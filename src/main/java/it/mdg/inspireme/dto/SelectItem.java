package it.mdg.inspireme.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SelectItem {
    private String label;
    private Integer value;
}
