package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.Dish;
import com.dudungtak.seproject.entity.Menu;
import com.dudungtak.seproject.entity.MenuElement;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.Pagination;
import com.dudungtak.seproject.network.request.MenuApiRequest;
import com.dudungtak.seproject.network.request.MenuElementApiRequest;
import com.dudungtak.seproject.network.response.MenuApiResponse;
import com.dudungtak.seproject.network.response.MenuElementApiResponse;
import com.dudungtak.seproject.repository.DishRepository;
import com.dudungtak.seproject.repository.MenuElementRepository;
import com.dudungtak.seproject.repository.MenuRepository;
import com.dudungtak.seproject.service.BaseCrudApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuApiService {
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuElementRepository menuElementRepository;

    @Autowired
    DishRepository dishRepository;

    public Header<MenuApiResponse> create(Header<MenuApiRequest> request) {
        MenuApiRequest body = request.getData();

        Menu menu = Menu.builder()
                .name(body.getName())
                .price(body.getTotalPrice())
                .imgUrl(body.getImgUrl())
                .image(body.getImage())
                .registeredAt(body.getRegisteredAt())
                .build();

        Menu savedMenu = menuRepository.save(menu);

        List<MenuElementApiResponse> menuElementApiResponseList = body.getMenuElementList().stream()
                .map(menuElementApiRequest -> {
                    Dish dish = dishRepository.getOne(menuElementApiRequest.getDishId());

                    MenuElement menuElement = MenuElement.builder()
                            .totalPrice(dish.getPrice().multiply(BigDecimal.valueOf(menuElementApiRequest.getQuantity())))
                            .quantity(menuElementApiRequest.getQuantity())
                            .menu(savedMenu)
                            .dish(dish)
                            .build();

                    return menuElementRepository.save(menuElement);
                })
                .map(MenuElementApiService::response)
                .collect(Collectors.toList());

        MenuApiResponse menuApiResponse = response(savedMenu, menuElementApiResponseList);

        return Header.OK(menuApiResponse);
    }

    public Header<MenuApiResponse> read(Long id) {
        return menuRepository.findById(id)
            .map(menu -> {
                List<MenuElementApiResponse> menuElementApiResponseList = menu.getMenuElementList().stream()
                    .map(MenuElementApiService::response)
                    .collect(Collectors.toList());

                return response(menu, menuElementApiResponseList);
            })
            .map(Header::OK)
            .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header<List<MenuApiResponse>> readAll(Pageable pageable) {
        Page<Menu> menus = menuRepository.findAll(pageable);

        List<MenuApiResponse> menuApiResponseList = menus.stream()
                .map(MenuApiService::response)
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(menus);

        return Header.OK(menuApiResponseList, pagination);
    }

    public Header<MenuApiResponse> update(Header<MenuApiRequest> request) {
        MenuApiRequest body = request.getData();

        return menuRepository.findById(body.getId())
                .map(menu -> {
                    menu
                            .setName(body.getName())
                            .setPrice(body.getTotalPrice())
                            .setImgUrl(body.getImgUrl())
                            .setImage(body.getImage())
                            .setRegisteredAt(body.getRegisteredAt())
                            .setUnregisteredAt(body.getUnregisteredAt());

                    Menu updatedMenu = menuRepository.save(menu);
                    System.out.println(updatedMenu);
                    // update MenuElement
                    List<MenuElementApiRequest> menuElementApiRequestList = body.getMenuElementList();


                    List<Long> menuElementIdList = updatedMenu.getMenuElementList().stream().
                            map(menuElement -> menuElement.getId())
                            .collect(Collectors.toList());

                    for(MenuElementApiRequest menuElementApiRequest : menuElementApiRequestList) {
                        Long id = menuElementApiRequest.getId();

                        // 기존 요리를 변경한 경우
                        if(menuElementIdList.contains(id)) {
                            Integer quantity = menuElementApiRequest.getQuantity();
                            // 기존 요리의 수량을 변경한 경우
                            if(quantity != 0) {
                                MenuElement menuElement = menuElementRepository.getOne(id)
                                        .setQuantity(menuElementApiRequest.getQuantity());
                                menuElementRepository.save(menuElement);
                            } else {        // 기존 요리를 삭제한 경우
                                MenuElement menuElement = menuElementRepository.getOne(id);
                                menuElementRepository.delete(menuElement);
                            }
                        } else {        // 새로운 요리를 추가한 경우
                            Dish dish = dishRepository.getOne(menuElementApiRequest.getDishId());

                            MenuElement menuElement = MenuElement.builder()
                                    .totalPrice(dish.getPrice().multiply(BigDecimal.valueOf(menuElementApiRequest.getQuantity())))
                                    .quantity(menuElementApiRequest.getQuantity())
                                    .menu(updatedMenu)
                                    .dish(dish)
                                    .build();

                            menuElementRepository.save(menuElement);
                        }
                    }

                    return updatedMenu;
                })
                .map(MenuApiService::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public Header delete(Long id) {
        return menuRepository.findById(id)
                .map(menu -> {
                    menu.getMenuElementList()
                            .forEach(menuElement -> menuElementRepository.delete(menuElement));

                    return menu;
                })
                .map(menu -> {
                    menuRepository.delete(menu);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("no data"));
    }

    public static MenuApiResponse response(Menu menu) {
        return MenuApiResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .totalPrice(menu.getPrice())
                .imgUrl(menu.getImgUrl())
                .image(menu.getImage())
                .registeredAt(menu.getRegisteredAt())
                .unregisteredAt(menu.getUnregisteredAt())
                .createdAt(menu.getCreatedAt())
                .createdBy(menu.getCreatedBy())
                .updatedAt(menu.getUpdatedAt())
                .updatedBy(menu.getUpdatedBy())
                .build();
    }

    public static MenuApiResponse response(Menu menu, List<MenuElementApiResponse> menuElementApiResponseList) {
        return MenuApiResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .totalPrice(menu.getPrice())
                .imgUrl(menu.getImgUrl())
                .image(menu.getImage())
                .registeredAt(menu.getRegisteredAt())
                .unregisteredAt(menu.getUnregisteredAt())
                .createdAt(menu.getCreatedAt())
                .createdBy(menu.getCreatedBy())
                .updatedAt(menu.getUpdatedAt())
                .updatedBy(menu.getUpdatedBy())
                .menuElementList(menuElementApiResponseList)
                .build();
    }
}
