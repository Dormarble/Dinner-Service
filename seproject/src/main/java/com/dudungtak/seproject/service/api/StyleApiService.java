package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Style;
import com.dudungtak.seproject.exception.BadInputException;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.StyleApiRequest;
import com.dudungtak.seproject.network.response.StyleApiResponse;
import com.dudungtak.seproject.repository.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StyleApiService {

    private final StyleRepository styleRepository;

    @Autowired
    public StyleApiService(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    public Header<StyleApiResponse> create(Header<StyleApiRequest> request) {
        StyleApiRequest body = request.getData();

        Style style = Style.builder()
                .name(body.getName())
                .price(body.getPrice())
                .status(body.getStatus())
                .content(body.getContent())
                .imgUrl(body.getImgUrl())
                .image(body.getImage())
                .registeredAt(LocalDate.now())
                .build();

        Style savedStyle = styleRepository.save(style);
        StyleApiResponse styleApiResponse = response(savedStyle);

        return Header.OK(styleApiResponse);
    }

    public Header<StyleApiResponse> read(Long id) {
        return styleRepository.findById(id)
                .map(StyleApiService::response)
                .map(Header::OK)
                .orElseThrow(BadInputException::new);
    }

    public Header<List<StyleApiResponse>> readAll() {
        List<StyleApiResponse> styleApiResponseList = styleRepository.findAll().stream()
                                                        .map(StyleApiService::response)
                                                        .collect(Collectors.toList());

        return Header.OK(styleApiResponseList);
    }


    public Header<StyleApiResponse> update(Header<StyleApiRequest> request) {
        StyleApiRequest body = request.getData();

        return styleRepository.findById(body.getId())
                .map(style -> {
                    style
                        .setName(body.getName())
                        .setPrice(body.getPrice())
                        .setStatus(body.getStatus())
                        .setContent(body.getContent())
                        .setImgUrl(body.getImgUrl())
                        .setImage(body.getImage());

                    if(body.getRegisteredAt() != null)
                        style.setRegisteredAt(body.getRegisteredAt());
                    if(body.getUnregisteredAt() != null)
                        style.setUnregisteredAt(body.getUnregisteredAt());

                    Style updatedStyle = styleRepository.save(style);

                    return updatedStyle;
                })
                .map(StyleApiService::response)
                .map(Header::OK)
                .orElseThrow(BadInputException::new);
    }

    public Header delete(Long id) {
        return styleRepository.findById(id)
                .map(dish -> {
                    styleRepository.delete(dish);
                    return Header.OK();
                })
                .orElseThrow(BadInputException::new);
    }

    public static StyleApiResponse response(Style style) {
        return StyleApiResponse.builder()
                .id(style.getId())
                .name(style.getName())
                .price(style.getPrice())
                .status(style.getStatus())
                .content(style.getContent())
                .imgUrl(style.getImgUrl())
                .image(style.getImage())
                .registeredAt(style.getRegisteredAt())
                .unregisteredAt(style.getUnregisteredAt())
                .createdAt(style.getCreatedAt())
                .createdBy(style.getCreatedBy())
                .updatedAt(style.getUpdatedAt())
                .updatedBy(style.getUpdatedBy())
                .build();
    }

}
