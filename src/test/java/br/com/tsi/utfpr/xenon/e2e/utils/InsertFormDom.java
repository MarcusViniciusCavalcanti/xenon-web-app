package br.com.tsi.utfpr.xenon.e2e.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import lombok.Setter;

public class InsertFormDom {

    private static InsertFormDom instance;

    private HtmlForm form;

    @Setter
    private HtmlPage htmlPage;

    public static InsertFormDom init(WebClient client, String url) throws IOException {
        instance = new InsertFormDom();
        var htmlPage = client.<HtmlPage>getPage(url);
        instance.setHtmlPage(htmlPage);
        return instance;
    }

    public InsertFormDom insertForm(String name) {
        form = htmlPage.getFormByName(name);
        return instance;
    }

    public InsertFormDom setInputValue(String input, String value) {
        form.getInputByName(input).setValueAttribute(value);
        return instance;
    }

    public HtmlPage clickSubmitButton() throws IOException {
        return form.getOneHtmlElementByAttribute("button", "type", "submit").click();
    }
}
