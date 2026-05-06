package grsu.by.entity;

import grsu.by.enums.RestaurantTableStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "restaurant_id")
    private Long restaurantId;

    @Column(name = "number")
    private String number;

    @Column(name = "capacity")
    @ColumnDefault("1")
    private Short capacity;

    @Column(name = "location")
    private String location;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RestaurantTableStatus status;

    @Column(name = "pos_x")
    @ColumnDefault("0")
    private Double posX;

    @Column(name = "pos_y")
    @ColumnDefault("0")
    private Double posY;

    @Column(name = "width")
    @ColumnDefault("10")
    private Double width;

    @Column(name = "height")
    @ColumnDefault("10")
    private Double height;

    @Column(name = "shape")
    @ColumnDefault("'rect'")
    private String shape;

    @Column(name = "label")
    private String label;
}
