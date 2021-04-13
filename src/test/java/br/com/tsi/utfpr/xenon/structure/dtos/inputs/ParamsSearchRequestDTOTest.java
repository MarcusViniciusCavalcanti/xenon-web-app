package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test - Unidade - ParamsSearchRequest")
class ParamsSearchRequestDTOTest {

    private static final String DEFAULT_SORT_DIRECT = "ASC";
    private static final String DEFAULT_SORT_PROPERTY = "name";
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_SIZE_ELEMENT = 5;
    private static final int PAGE_10 = 10;
    private static final int SIZE_50 = 50;

    @Test
    @DisplayName("Deve valores padrões")
    void shouldReturnObjectWithDefaultValue() {
        var paramsSearchRequestDTO = new ParamsSearchRequestDTO();

        assertEquals(DEFAULT_PAGE_NUMBER, paramsSearchRequestDTO.getOrDefaultPage());
        assertEquals(DEFAULT_SORT_DIRECT, paramsSearchRequestDTO.getOrDefaultSortDirection());
        assertEquals(DEFAULT_SORT_PROPERTY, paramsSearchRequestDTO.getOrDefaultSort());
        assertTrue(paramsSearchRequestDTO.getOrDefaultEnabled());
        assertEquals(DEFAULT_SIZE_ELEMENT, paramsSearchRequestDTO.getOrDefaultSize());
    }

    @Test
    @DisplayName("Deve valores oriundos da request")
    void shouldReturnObjectWithCustomValue() {
        var paramsSearchRequestDTO = new ParamsSearchRequestDTO();

        paramsSearchRequestDTO.setEnabled( Boolean.FALSE);
        var page = PAGE_10;
        paramsSearchRequestDTO.setPage(page);
        var size = SIZE_50;
        paramsSearchRequestDTO.setSize(size);
        var sort = "test sort";
        paramsSearchRequestDTO.setSort(sort);
        var sortDirection = "DESC";
        paramsSearchRequestDTO.setSortDirection(sortDirection);

        assertEquals(page, paramsSearchRequestDTO.getOrDefaultPage());
        assertEquals(sortDirection, paramsSearchRequestDTO.getOrDefaultSortDirection());
        assertEquals(sort, paramsSearchRequestDTO.getOrDefaultSort());
        assertFalse(paramsSearchRequestDTO.getOrDefaultEnabled());
        assertEquals(size, paramsSearchRequestDTO.getOrDefaultSize());
    }
}
