package grsu.by.entity;

import grsu.by.enums.RestaurantStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "rating")
    private BigDecimal rating = BigDecimal.ZERO;
    @Column(name = "preview_photo_url")
    private String previewPhotoUrl;
    @Column(name = "contact_phone")
    private String contactPhone;
    @Column(name = "working_hours")
    private String workingHours;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;
    @Column(name = "owner_email")
    private String ownerEmail;
    @Embedded
    private Address address;
}
