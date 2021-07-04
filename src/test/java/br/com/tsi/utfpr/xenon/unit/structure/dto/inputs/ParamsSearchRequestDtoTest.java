package br.com.tsi.utfpr.xenon.unit.structure.dto.inputs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test - Unidade - ParamsSearchRequest")
class ParamsSearchRequestDtoTest {

    private static final String DEFAULT_SORT_DIRECT = "DESC";
    private static final String DEFAULT_SORT_PROPERTY = "createdAt";
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_SIZE_ELEMENT = 5;
    private static final int PAGE_10 = 10;
    private static final int SIZE_50 = 50;

    @Test
    @DisplayName("Deve valores padrões")
    void shouldReturnObjectWithDefaultValue() {
        var paramsSearchRequestDTO = new ParamsSearchRequestDto();

        assertEquals(DEFAULT_PAGE_NUMBER, paramsSearchRequestDTO.getOrDefaultPage());
        assertEquals(DEFAULT_SORT_DIRECT, paramsSearchRequestDTO.getOrDefaultSortDirection());
        assertEquals(DEFAULT_SORT_PROPERTY, paramsSearchRequestDTO.getOrDefaultSort());
        assertTrue(paramsSearchRequestDTO.getOrDefaultEnabled());
        assertEquals(DEFAULT_SIZE_ELEMENT, paramsSearchRequestDTO.getOrDefaultSize());
    }

    @Test
    @DisplayName("Deve valores oriundos da request")
    void shouldReturnObjectWithCustomValue() {
        var paramsSearchRequestDTO = new ParamsSearchRequestDto();

        paramsSearchRequestDTO.setEnabled( Boolean.FALSE);
        var page = PAGE_10;
        paramsSearchRequestDTO.setPage(page);
        var size = SIZE_50;
        paramsSearchRequestDTO.setSize(size);
        var sort = "test sort";
        paramsSearchRequestDTO.setSort(sort);
        var sortDirection = "DESC";
        paramsSearchRequestDTO.setSortDirection(sortDirection);
        var name = "name";
        paramsSearchRequestDTO.setName(name);
        var profile = "profile";
        paramsSearchRequestDTO.setProfile(profile);
        var type = "type";
        paramsSearchRequestDTO.setType(type);

        assertEquals(page, paramsSearchRequestDTO.getOrDefaultPage());
        assertEquals(sortDirection, paramsSearchRequestDTO.getOrDefaultSortDirection());
        assertEquals(sort, paramsSearchRequestDTO.getOrDefaultSort());
        assertFalse(paramsSearchRequestDTO.getOrDefaultEnabled());
        assertEquals(size, paramsSearchRequestDTO.getOrDefaultSize());
        assertEquals(type, paramsSearchRequestDTO.getType());
        assertEquals(profile, paramsSearchRequestDTO.getProfile());
        assertEquals(name, paramsSearchRequestDTO.getName());
    }
}
