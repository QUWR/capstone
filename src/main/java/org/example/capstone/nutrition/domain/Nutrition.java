package org.example.capstone.nutrition.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Nutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double calories;        //칼로리
    private Double carbohydrate;    //탄수화물
    private Double protein;    //단백질
    private Double fat;    //지방
    private Double sugar;   //당
    private Double sodium;   //나트륨
    private Double saturatedFat;    //포화지방
    private Double transFat;    //트랜스지방
    private Double cholesterol;     //콜레스트롤
}
