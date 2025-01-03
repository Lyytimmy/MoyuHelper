package cn.lyy.niukeinidea.Factory;

import cn.lyy.niukeinidea.api.NowcoderApi;

import cn.lyy.niukeinidea.enums.Company;
import cn.lyy.niukeinidea.enums.JobType;
import cn.lyy.niukeinidea.model.request.MianJinRequest;
import cn.lyy.niukeinidea.utils.MarkdownUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;


public class MarkdownToolWindowFactory implements ToolWindowFactory {

    private final NowcoderApi newcoderAPI = new NowcoderApi();

    private MianJinRequest request = new MianJinRequest();

    private AtomicBoolean isLoading = new AtomicBoolean(false);
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建右版
        JBCefBrowser browser = new JBCefBrowser();
        JBScrollPane browserScrollPane = new JBScrollPane(browser.getComponent(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // 创建左版
        JPanel leftPanel = new JPanel(new BorderLayout());
        // 创建输入框
        JTextArea markdownInputArea = new JTextArea(10, 40);
        markdownInputArea.setBorder(JBUI.Borders.empty(10));
        JBScrollPane inputScrollPane = new JBScrollPane(markdownInputArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inputScrollPane.setPreferredSize(new Dimension(400, 200)); // 设置首选大小
        inputScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        leftPanel.add(inputScrollPane, BorderLayout.CENTER);

        // 创建参数版
        JPanel parameterPanel = new JPanel(new FlowLayout());
        // 岗位下拉框组件
        JLabel jobLabel = new JLabel("岗位：");
        ComboBox<String> jobComboBox = new ComboBox<>(new String[]{"全部岗位", "前端", "后端", "客户端"});
        jobComboBox.setSelectedIndex(0);
        jobComboBox.addActionListener(e -> {
            String selectedJob = (String) jobComboBox.getSelectedItem();
            Integer jobId = JobType.getJobId(selectedJob);
            request.setJobType(jobId);
        });
        parameterPanel.add(jobLabel);
        parameterPanel.add(jobComboBox);

        // 公司下拉框
        JLabel companyLabel = new JLabel("公司：");
        ComboBox<String> companyComboBox = new ComboBox<>(new String[]{"", "阿里巴巴", "腾讯", "百度", "小米集团"});
        companyComboBox.setSelectedIndex(0);
        companyComboBox.addActionListener(e -> {
            String selectedCompany = (String) companyComboBox.getSelectedItem();
            Integer companyId = null;
            if (selectedCompany != null && !selectedCompany.isEmpty()) {
                companyId = Company.getId(selectedCompany);
            }
            ArrayList<Integer> companyList = new ArrayList<>();
            companyList.add(companyId);
            request.setCompanyList(companyList);
        });
        parameterPanel.add(companyLabel);
        parameterPanel.add(companyComboBox);

        // 搜索按钮组件
        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> {
            String markdownContent;
            try {
                markdownContent = newcoderAPI.getMainJin(request);
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            };
            markdownInputArea.setText(markdownContent);
            markdownContent = MarkdownUtil.convertMarkdownToHtml(markdownContent);
            browser.loadHTML(markdownContent);
        });
        parameterPanel.add(searchButton);

        // 渲染按钮
        JButton renderButton = new JButton("渲染");
        renderButton.addActionListener(e -> {
            String markdownContent = markdownInputArea.getText();
            markdownContent = MarkdownUtil.convertMarkdownToHtml(markdownContent);
            browser.loadHTML(markdownContent);
        });
        parameterPanel.add(renderButton);

        leftPanel.add(parameterPanel, BorderLayout.NORTH);

        // 添加滚动事件监听器
        inputScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            JScrollBar verticalScrollBar = inputScrollPane.getVerticalScrollBar();
             isLoading.set(false);
            JLabel loadingLabel = new JLabel("正在加载，请稍候...");
            loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
            leftPanel.add(loadingLabel, BorderLayout.SOUTH);
            loadingLabel.setVisible(false);
            if (!verticalScrollBar.getValueIsAdjusting() &&
                    verticalScrollBar.getValue() + verticalScrollBar.getVisibleAmount() == verticalScrollBar.getMaximum()) {
                // 等待3s，防止持续生成的内容
                // 创建一个定时器，在3秒后执行任务
                if (isLoading.get()){
                    return;
                }
                isLoading.set(true);
                // 动画没效果
                loadingLabel.setVisible(true);
                TimerTask task = new TimerTask() {
                    public void run() {
                        // 已经滚动到底部，加载下一页内容
                        int currentPage = request.getPage() + 1;
                        request.setPage(currentPage);
                        String markdownContent;
                        try {

                            markdownContent = newcoderAPI.getMainJin(request);
                        } catch (IOException | InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }

                        // 记录当前滚动位置
                        int currentScrollPosition = verticalScrollBar.getValue();

                        // 追加新内容
                        markdownInputArea.append(markdownContent);

                        // 更新浏览器内容
                        markdownContent = MarkdownUtil.convertMarkdownToHtml(markdownInputArea.getText());
                        browser.loadHTML(markdownContent);

                        // 恢复滚动位置
                        verticalScrollBar.setValue(currentScrollPosition);

                        isLoading.set(false);
                        loadingLabel.setVisible(false);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 3000);

            }
        });

        // 主板左边是左版，右边是JCEF浏览器用来渲染Markdown
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(browserScrollPane, BorderLayout.CENTER);

        toolWindow.getComponent().add(mainPanel);
    }
}

