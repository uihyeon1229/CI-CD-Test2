package com.project.sangil_be.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class SearchDto {

    private Long mountainId;
    private String mountain;
    private String mountainAddress;
    private String mountainImgUrl;
    private Double starAvr;
    private Double lat;
    private Double lng;

    // querydsl constructor
    public SearchDto(Long mountainId, String mountain, String mountainAddress, String mountainImgUrl, Double starAvr, Double lat, Double lng) {
        this.mountainId = mountainId;
        this.mountain = mountain;
        this.mountainAddress = mountainAddress;
        this.mountainImgUrl = mountainImgUrl;
        if (starAvr == null) {
            this.starAvr = 0D;
        } else {
            this.starAvr = Math.round(starAvr*100)/100.0;
        }
        this.lat = lat;
        this.lng = lng;
    }

//    public SearchDto(String starAvr, Mountain100Dto mountain100Dto) {
//        this.mountainId=mountain100Dto.getMountainId();
//        this.mountain=mountain100Dto.getMountain();
//        this.mountainAddress=mountain100Dto.getMountainAddress();
//        this.mountainImgUrl=mountain100Dto.getMountainImgUrl();
//        this.starAvr=starAvr;
//        this.lat=mountain100Dto.getLat();
//        this.lng=mountain100Dto.getLng();
//    }

    public SearchDto(Map<String, Object> stringObjectMap) {
        this.mountainId = Long.valueOf(String.valueOf(stringObjectMap.get("mountain_id")));
        this.mountain = String.valueOf(stringObjectMap.get("mountain"));
        this.mountainAddress = String.valueOf(stringObjectMap.get("mountain_address"));
        this.mountainImgUrl = String.valueOf(stringObjectMap.get("mountain_img_url"));
        if (stringObjectMap.get("avrStar")==null) {
            this.starAvr = 0d;
        } else {
            this.starAvr = Math.round(Double.valueOf(String.valueOf(stringObjectMap.get("avrStar")))*100)/100.0;
        }
        this.lat = Double.valueOf(String.valueOf(stringObjectMap.get("lat")));
        this.lng = Double.valueOf(String.valueOf(stringObjectMap.get("lng")));
    }

}

