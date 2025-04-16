package com.gbsb.routie_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String hair;
    private String outfit;
    private String Bottom;
    private String accessory;
    private String Shoes;
    private String background;
}
