package br.com.tsi.utfpr.xenon.e2e.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.tsi.utfpr.xenon.domain.config.property.FilesProperty;
import br.com.tsi.utfpr.xenon.e2e.AbstractEndToEndTest;
import br.com.tsi.utfpr.xenon.e2e.utils.GetElementDom;
import br.com.tsi.utfpr.xenon.e2e.utils.InsertFormDom;
import br.com.tsi.utfpr.xenon.e2e.utils.StreamUtils;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.html.DomNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.context.WebApplicationContext;


@DisplayName("Test - e2e - Funcionalidade perfil Usuários")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_insert.sql"})
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = {
    "classpath:/sql/user_default_delete.sql"})
class FeatureProfileUserTest extends AbstractEndToEndTest {

    private static final String ATTRIBUTE_CLASS = "class";
    private static final String SECTION = "section";
    private static final String url = "http://localhost:8080/perfil";
    private static final String ATTRIBUTE_ID = "id";
    private static final String MESSAGE_ERROR = "message_error";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilesProperty filesProperty;

    @Test
    @DisplayName("Deve exibir sidebar com informações do usuário")
    @WithUserDetails(value = "beltrano_with_car@user.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldReturnAvatar() throws IOException {
        GetElementDom.start(webClient, url)
            .navigation(SECTION, ATTRIBUTE_CLASS, "profile-env")
            .getDiv(ATTRIBUTE_CLASS, "user-info-sidebar")
            .executeAssertion(div -> {
                var divImg = GetElementDom.getDiv(div, ATTRIBUTE_CLASS, "user-img");
                var img = GetElementDom
                    .getImg(divImg, ATTRIBUTE_CLASS, "img-cirlce img-responsive img-thumbnail");
                assertEquals("/user/avatar/200", img.getSrcAttribute());

                var divName = GetElementDom.getDiv(div, ATTRIBUTE_CLASS, "user-name");
                assertEquals("Beltrano com carro", divName.getVisibleText());

                var spanTypeUser = GetElementDom.getSpan(div, ATTRIBUTE_CLASS, "user-title");
                assertEquals("Estudante", spanTypeUser.getVisibleText());

                var ulRoles =
                    GetElementDom.getUl(div, ATTRIBUTE_CLASS, "list-unstyled user-info-list");
                assertEquals("Perfil Motorista", ulRoles.getVisibleText());
            });
    }

    @Test
    @DisplayName("Deve exibir as informações do status do cadastro com 100% quando cadastro está completo")
    @WithUserDetails(value = "beltrano_with_car@user.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveShowStatsRegistry() throws IOException {
        GetElementDom.start(webClient, url)
            .navigation(SECTION, ATTRIBUTE_CLASS, "profile-env")
            .getSection(ATTRIBUTE_CLASS, "user-timeline-stories")
            .getDiv(ATTRIBUTE_ID, "info_registry_value")
            .executeAssertion(div -> assertEquals("100%\nCadastro Completo", div.getVisibleText()));
    }

    @Test
    @DisplayName("Deve exibir as informações que falta cadastrar quando o cadastro não está em 100%")
    @WithUserDetails(value = "beltrano_without_car@user.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveShowMissingRegistryWhenStatusRegistryNotComplete() throws IOException {
        GetElementDom.start(webClient, url)
            .navigation(SECTION, ATTRIBUTE_CLASS, "profile-env")
            .getSection(ATTRIBUTE_CLASS, "user-timeline-stories")
            .getDiv(ATTRIBUTE_ID, "info_registry_value")
            .executeAssertion(div -> {
                var divProgress =
                    GetElementDom.getDiv(div, ATTRIBUTE_CLASS, "progress-bar progress-bar-success");
                assertEquals("40%", divProgress.getVisibleText());

                var ulItemsMissing =
                    GetElementDom.getUl(div, ATTRIBUTE_CLASS, "list-group list-group-minimal");
                var liList = StreamUtils.asStream(ulItemsMissing.getChildElements().iterator())
                    .map(DomNode::getVisibleText)
                    .collect(Collectors.toList());
                assertEquals(3, ulItemsMissing.getChildElementCount());
                assertThat(liList,
                    containsInAnyOrder("Não Adicionado Avatar", "Não Adicionado Dados do carro",
                        "Não Adicionado Documento do carro"));
            });
    }

    @Test
    @DisplayName("Deve exibir a informação sem registo quando carro não cadastrado")
    @WithUserDetails(value = "beltrano_without_car@user.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveShowMissingInformationCar() throws IOException {
        GetElementDom.start(webClient, url)
            .navigation(SECTION, ATTRIBUTE_CLASS, "profile-env")
            .getSection(ATTRIBUTE_CLASS, "user-timeline-stories")
            .getDiv(ATTRIBUTE_ID, "info_car_value")
            .executeAssertion(div -> assertEquals("Nenhum registro", div.getVisibleText()));

    }

    @Test
    @DisplayName("Deve exibir a informações do carro")
    @WithUserDetails(value = "beltrano_with_car@user.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveShowInformationCar() throws IOException {
        GetElementDom.start(webClient, url)
            .navigation(SECTION, ATTRIBUTE_CLASS, "profile-env")
            .getSection(ATTRIBUTE_CLASS, "user-timeline-stories")
            .getDiv(ATTRIBUTE_ID, "info_car_value")
            .getDiv(ATTRIBUTE_ID, "car_info")
            .executeAssertion(div -> {
                var tbodyInfoCar = GetElementDom.tbody(div, ATTRIBUTE_ID, "tbody_info_car");

                var infoModel = GetElementDom.tbRow(tbodyInfoCar, ATTRIBUTE_ID, "info_model");
                assertEquals("Gol Ok", infoModel.getVisibleText());

                var infoPlate = GetElementDom.tbRow(tbodyInfoCar, ATTRIBUTE_ID, "info_plate");
                assertEquals("ABC9801 Ok", infoPlate.getVisibleText());

                var infoDocument = GetElementDom.tbRow(tbodyInfoCar, ATTRIBUTE_ID, "info_document");
                assertEquals("Documento Ok", infoDocument.getVisibleText());
            });
    }

    @Test
    @DisplayName("Deve exibir as informações dos ultimos acessos")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveShowInformationLastAccess() throws IOException {
        GetElementDom.start(webClient, url)
            .navigation(SECTION, ATTRIBUTE_CLASS, "profile-env")
            .getSection(ATTRIBUTE_CLASS, "user-timeline-stories")
            .getDiv(ATTRIBUTE_ID, "info_access_value")
            .executeAssertion(div -> assertEquals("Nenhum registro", div.getVisibleText()));

    }

    @Test
    @DisplayName("Não Deve atualizar avatar")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldNotUpdateAvatar() throws IOException {
        try {
            InsertFormDom.init(webClient, url)
                .insertForm("avatar_upload")
                .setInputValue("file",
                    Files.createTempFile("testes", ".pdf").toFile().getAbsolutePath());

        } catch (FailingHttpStatusCodeException | ScriptException e) {
            assertTrue(e.getMessage().contains("422 Unprocessable Entity"));
            assertTrue(e.getMessage().contains("/user/avatar/upload/"));
        }
    }

    @Test
    @DisplayName("Deve atualiar o avatar do usuário")
    @WithUserDetails(value = "beltrano_admin@admin.com", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldHaveUpdateAvatar() throws IOException {
        var filePathTest = String.format("%s/153.png", filesProperty.getAvatarUrl());
        Files.deleteIfExists(new File(filePathTest).toPath());

        var tmpImage = Files.createTempFile("image", ".png");
        Files.copy(
            Objects.requireNonNull(
                FeatureAllUserTest.class.getResourceAsStream("/test-file/avatar/0.png")),
            tmpImage.toAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);

        InsertFormDom.init(webClient, url)
            .insertForm("avatar_upload")
            .setInputValue("file", tmpImage.toFile().getAbsolutePath());

        var path = new File(filePathTest).toPath();
        assertTrue(Files.exists(path));
    }

    @Override
    protected WebApplicationContext getWebApplicationContext() {
        return webApplicationContext;
    }
}
