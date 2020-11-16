package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Style;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.StyleApiRequest;
import com.dudungtak.seproject.network.response.StyleApiResponse;
import com.dudungtak.seproject.repository.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class StyleApiService {
    @Autowired
    StyleRepository styleRepository;

    public Header<StyleApiResponse> create(Header<StyleApiRequest> request) {
        StyleApiRequest body = request.getData();

        Style style = Style.builder()
                .name(body.getName())
                .price(body.getPrice())
                .status(body.getStatus())
                .content(body.getContent())
                .registeredAt(LocalDate.now())
                .build();

        Style savedStyle = styleRepository.save(style);

        return Optional.ofNullable(savedStyle)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("error on store"));
    }

    public Header<StyleApiResponse> read(Long id) {
        return styleRepository.findById(id)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no data"));

    }

    public Header<StyleApiResponse> update(Header<StyleApiRequest> request) {
        StyleApiRequest body = request.getData();

        return styleRepository.findById(body.getId())
                .map(style -> {
                    style
                            .setName(body.getName())
                            .setPrice(body.getPrice())
                            .setStatus(body.getStatus())
                            .setContent(body.getContent());

                    if(body.getRegisteredAt() != null)
                        style.setRegisteredAt(body.getRegisteredAt());
                    if(body.getUnregisteredAt() != null)
                        style.setUnregisteredAt(body.getUnregisteredAt());

                    Style updatedStyle = styleRepository.save(style);

                    return updatedStyle;
                })
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header delete(Long id) {
        return styleRepository.findById(id)
                .map(dish -> {
                    styleRepository.delete(dish);
                    return Header.OK("");
                })
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public StyleApiResponse response(Style style) {
        return StyleApiResponse.builder()
                .id(style.getId())
                .name(style.getName())
                .price(style.getPrice())
                .status(style.getStatus())
                .content(style.getContent())
                .registeredAt(style.getRegisteredAt())
                .unregisteredAt(style.getUnregisteredAt())
                .createdAt(style.getCreatedAt())
                .createdBy(style.getCreatedBy())
                .updatedAt(style.getUpdatedAt())
                .updatedBy(style.getUpdatedBy())
                .build();
    }
}
