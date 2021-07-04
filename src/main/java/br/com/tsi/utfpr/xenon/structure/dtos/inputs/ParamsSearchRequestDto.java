package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Setter
@NoArgsConstructor
@ToString
public class ParamsSearchRequestDto {

    public static final String DEFAULT_SORT_DIRECT = "DESC";
    public static final String DEFAULT_SORT_PROPERTY = "createdAt";
    public static final Boolean DEFAULT_ENABLED = Boolean.TRUE;
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_SIZE_ELEMENT = 5;

    private int page;
    private int size;
    private String sortDirection;
    private String sort;

    @Getter
    private String name;

    @Getter
    private String profile;

    @Getter
    private String type;

    private Boolean enabled;

    public int getOrDefaultPage() {
        if (page <= 0) {
            return DEFAULT_PAGE_NUMBER;
        }

        return page;
    }

    public int getOrDefaultSize() {
        if (size <= 0) {
            return DEFAULT_SIZE_ELEMENT;
        }

        return size;
    }

    public String getOrDefaultSortDirection() {
        if (StringUtils.isEmpty(sortDirection)) {
            return DEFAULT_SORT_DIRECT;
        }

        return sortDirection;
    }

    public String getOrDefaultSort() {
        if (StringUtils.isEmpty(sort)) {
            return DEFAULT_SORT_PROPERTY;
        }

        return sort;
    }

    public Boolean getOrDefaultEnabled() {
        if (Objects.isNull(enabled)) {
            return DEFAULT_ENABLED;
        }

        return enabled;
    }
}
