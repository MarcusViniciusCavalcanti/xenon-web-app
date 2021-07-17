package br.com.tsi.utfpr.xenon.e2e.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.Setter;

public class GetElementDom {

    private static final String IMG = "//img[@%s='%s']";
    private static final String SPAN = "//span[@%s='%s']";
    private static final String PARAGRAPH = "//p[@%s='%s']";
    private static final String TABLE_ROW = "//tr[@%s='%s']";
    private static final String TBODY = "//tbody[@%s='%s']";
    private static final String DIV = "//div[@%s='%s']";
    private static final String SECTION = "//section[@%s='%s']";
    private static final String TABLE_HEADER = "//th";
    private static final String UNORDERED_LIST = "//ul[@%s='%s']";
    private static final String LIST_ITEM = "//li[@%s='%s']";
    private static final String ANCHOR = "//a[@%s='%s']";
    private static final String SELECT = "//select[@%s='%s']";
    private static final String BUTTON = "//button[@%s='%s']";

    private static GetElementDom instance;

    @Setter
    private HtmlPage htmlPage;

    private HtmlElement element;

    public GetElementDom navigation(String parentElement, String attribute, String valueAttribute) {
        String xPath = String.format("//%s[@%s='%s']", parentElement, attribute, valueAttribute);
        element = htmlPage.getFirstByXPath(xPath);
        return instance;
    }

    public GetElementDom getChildUl(String attribute, String valueAttribute) {
        String xPath = String.format(UNORDERED_LIST, attribute, valueAttribute);
        element = element.<HtmlUnorderedList>getFirstByXPath(xPath);
        return instance;
    }

    public GetElementDom getChildLi(String attribute, String valueAttribute) {
        String xPath = String.format(LIST_ITEM, attribute, valueAttribute);
        element = element.<HtmlListItem>getFirstByXPath(xPath);
        return instance;
    }

    public GetElementDom getChildAnchor(String attribute, String valueAttribute) {
        String xPath = String.format(ANCHOR, attribute, valueAttribute);
        element = element.getFirstByXPath(xPath);
        return instance;

    }

    public GetElementDom getDiv(String attribute, String valueAttribute) {
        return getGetElementDom(attribute, valueAttribute, DIV);
    }

    public GetElementDom getTbody(String attribute, String valueAttribute) {
        return getGetElementDom(attribute, valueAttribute, TBODY);
    }

    public GetElementDom getTr(String attribute, String valueAttribute) {
        return getGetElementDom(attribute, valueAttribute, TABLE_ROW);
    }

    public GetElementDom getSpan(String attribute, String valueAttribute) {
        return getGetElementDom(attribute, valueAttribute, SPAN);
    }

    public GetElementDom getP(String attribute, String valueAttribute) {
        return getGetElementDom(attribute, valueAttribute, PARAGRAPH);
    }

    public GetElementDom getSection(String attribute, String valueAttribute) {
        return getGetElementDom(attribute, valueAttribute, SECTION);
    }

    public GetElementDom getButton(String attribute, String valueAttribute) {
        return getGetElementDom(attribute, valueAttribute, BUTTON);
    }

    public GetElementDom getTh() {
        element = element.getFirstByXPath("//th");
        return instance;
    }

    public List<DomElement> listAllTh() {
        return element.getByXPath(TABLE_HEADER);
    }

    public GetElementDom setAttributeInElement(String attribute, String valueAttribute) {
        element.setAttribute(attribute, valueAttribute);
        return instance;
    }

    public GetElementDom awaitPage(long value) {
        htmlPage.getWebClient().waitForBackgroundJavaScript(value);
        return instance;
    }

    public GetElementDom getSelect(String attribute, String valueAttribute) {
        return getGetElementDom(attribute, valueAttribute, SELECT);
    }

    public HtmlElement getElement() {
        return element;
    }

    public List<DomElement> getListChildSpan(String attribute, String valueAttribute) {
        var xPath = String.format(SPAN, attribute, valueAttribute);
        return element.getByXPath(xPath);
    }

    public List<DomElement> getListElement(String attribute, String value) {
        var childElements = element.getChildElements();
        return StreamUtils.asStream(childElements.iterator())
            .collect(Collectors.toList());
    }

    public DomNodeList<HtmlElement> getListChildAnchor() {
        return element.getElementsByTagName("a");
    }

    public HtmlPage getHtmlPage() {
        return htmlPage;
    }

    public HtmlPage redirectOnClick() throws IOException {
        return element.click();
    }

    public GetElementDom executeAssertion(Consumer<HtmlElement> assertion) {
        assertion.accept(element);
        return instance;
    }

    private GetElementDom getGetElementDom(String attribute, String valueAttribute, String s) {
        var xPath = getXpath(attribute, valueAttribute, s);
        element = element.getFirstByXPath(xPath);
        return instance;
    }

    public static GetElementDom start(WebClient client, String url) throws IOException {
        instance = new GetElementDom();
        var htmlPage = client.<HtmlPage>getPage(url);
        instance.setHtmlPage(htmlPage);
        return instance;
    }

    public static GetElementDom start(HtmlPage page) {
        instance = new GetElementDom();
        instance.setHtmlPage(page);
        return instance;
    }

    public static HtmlDivision getDiv(HtmlElement htmlElement, String attribute,
        String valueAttribute) {
        return htmlElement.getFirstByXPath(getXpath(attribute, valueAttribute, DIV));
    }

    public static HtmlImage getImg(HtmlElement el, String attribute, String valueAttribute) {
        return el.getFirstByXPath(getXpath(attribute, valueAttribute, IMG));
    }

    public static HtmlSpan getSpan(HtmlElement element, String attribute, String valueAttribute) {
        return element.getFirstByXPath(getXpath(attribute, valueAttribute, SPAN));
    }

    private static String getXpath(String attribute, String valueAttribute, String elementName) {
        return String.format(elementName, attribute, valueAttribute);
    }

    public static HtmlUnorderedList getUl(HtmlElement element, String attribute,
        String valueAttribute) {
        return element.getFirstByXPath(getXpath(attribute, valueAttribute, UNORDERED_LIST));
    }

    public static HtmlTableBody tbody(HtmlElement element, String attribute,
        String valueAttribute) {
        return element.getFirstByXPath(getXpath(attribute, valueAttribute, TBODY));
    }

    public static HtmlTableRow tbRow(HtmlTableBody tableBody, String attribute,
        String valueAttribute) {
        return tableBody.getFirstByXPath(getXpath(attribute, valueAttribute, TABLE_ROW));
    }

    public static HtmlTableRow tbRowByIndex(HtmlElement tbody, String attribute,
        String valueAttribute, int index) {

        return tbody.<HtmlTableRow>getByXPath(getXpath(attribute, valueAttribute, TABLE_ROW))
            .get(index);
    }

    public static HtmlButton getButton(HtmlElement element, String attribute,
        String valueAttribute) {
        return element.getFirstByXPath(getXpath(attribute, valueAttribute, BUTTON));
    }
}
