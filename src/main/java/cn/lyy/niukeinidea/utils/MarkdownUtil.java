package cn.lyy.niukeinidea.utils;

import cn.lyy.niukeinidea.model.MianJinModel;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

public class MarkdownUtil {
    /**
     * 转换成markdown格式
     * @param list
     * @return
     */
    public static String MainJinMarkdown(List<MianJinModel> list) {
        StringBuilder markdown = new StringBuilder();
        for (MianJinModel model : list) {
            markdown.append("- 作者: ").append(model.getName()).append("\n");
            markdown.append("- 学校: ").append(model.getEducationInfo()).append("\n");
            markdown.append("- 时间: ").append(model.getDate()).append("\n");
            markdown.append("- 内容: ").append(model.getContent()).append("\n");
            markdown.append("\n");
        }
        return markdown.toString();
    }
    public static String pretendToClass(List<MianJinModel> list) {
        StringBuilder markdown = new StringBuilder();
        for (MianJinModel model : list) {
            markdown.append("public class MainJin {").append("\n");
            markdown.append("private String author: ").append(model.getName()).append("\n");
            markdown.append("private String school: ").append(model.getEducationInfo()).append("\n");
            markdown.append("private String time: ").append(model.getDate()).append("\n");
            markdown.append("public static class content {: ").append(model.getContent()).append("\n");
            markdown.append("}").append("\n");
            markdown.append("}").append("\n");
            markdown.append("\n");
        }
        return markdown.toString();
    }

    /**
     * 将 Markdown 转换为 HTML 的方法
     * @param markdownContent Markdown 格式的字符串
     * @return HTML 格式的字符串
     */
    public static String convertMarkdownToHtml(String markdownContent) {
        // 使用 Flexmark 将 Markdown 转换为 HTML
        com.vladsch.flexmark.parser.Parser parser = com.vladsch.flexmark.parser.Parser.builder().build();
        com.vladsch.flexmark.html.HtmlRenderer renderer = com.vladsch.flexmark.html.HtmlRenderer.builder().build();
        String htmlContent = renderer.render(parser.parse(markdownContent));

        // 添加自定义样式
        String customStyles = getCustomStyles();
        return "<html><head>" + customStyles + "</head><body>" + htmlContent + "</body></html>";
    }

    /**
     * 添加样式
     * @return
     */
    public static String getCustomStyles() {
        return "<style>"
                + "body {"
                + "  color: white;"
                + "  font-family: 'JetBrains Mono', monospace;" // 使用与IDEA相似的等宽字体
                + "  font-size: 14px;" // 设置字体大小为14px，可以根据实际情况调整
                + "}"
                + "</style>";

    }


}
