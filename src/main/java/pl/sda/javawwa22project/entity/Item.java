package pl.sda.javawwa22project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long id;
    private String itemName;
    private String description;
    private String category;
    private BigDecimal price;
    private int quantity;
    private String picture;
}
