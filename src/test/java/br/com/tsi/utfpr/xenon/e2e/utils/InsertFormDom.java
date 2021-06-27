package br.com.tsi.utfpr.xenon.e2e.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.List;
import lombok.Setter;

public class InsertFormDom {

    private static InsertFormDom instance;

    private HtmlForm form;

    private String formName;

    private WebClient webClient;

    @Setter
    private HtmlPage htmlPage;

    private InsertFormDom(WebClient webClient) {
        this.webClient = webClient;
    }

    public InsertFormDom insertForm(String name) {
        formName = name;
        form = htmlPage.getFormByName(name);
        return instance;
    }

    public InsertFormDom setInputValue(String input, String value) {
        form.getInputByName(input).setValueAttribute(value);
        return instance;
    }

    public InsertFormDom setSelect(String select, int index) {
        form.getSelectByName(select).setSelectedIndex(index);
        return instance;
    }

    public InsertFormDom setSelectMultiple(String select, List<? extends Number> values) {
        var authorities = form.getSelectByName(select);
        values.forEach(index -> authorities.setSelectedIndex(index.intValue()));
        return instance;
    }

    public HtmlPage clickSubmitButton() throws IOException {
        return form.getOneHtmlElementByAttribute("button", "type", "submit").click();
    }

    public InsertFormDom clickButton() throws IOException {
        htmlPage = clickSubmitButton();
        return instance;
    }

    public InsertFormDom clickButtonType() throws IOException {
        htmlPage = form.getOneHtmlElementByAttribute("button", "type", "button").click();
        return instance;
    }

    public GetElementDom navigateForm() {
        return GetElementDom.start(htmlPage)
            .navigation("form", "name", formName);
    }

    public GetElementDom navigate(String parentName, String attribute, String attributeValue) {
        return GetElementDom.start(htmlPage)
            .navigation(parentName, attribute, attributeValue);
    }

    public static InsertFormDom init(WebClient client, String url) throws IOException {
        instance = new InsertFormDom(client);
        var htmlPage = instance.webClient.<HtmlPage>getPage(url);
        instance.setHtmlPage(htmlPage);
        return instance;
    }
}
