package br.com.tsi.utfpr.xenon.web.error;

public class WebException extends RuntimeException {

    public WebException(Class<?> controller) {
        super(String.format("error in controler %s", controller.getTypeName()));
    }

}
