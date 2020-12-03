package com.dudungtak.seproject.domain.service.api;

import com.dudungtak.seproject.model.entity.Dish;
import com.dudungtak.seproject.model.entity.Menu;
import com.dudungtak.seproject.model.entity.MenuElement;
import com.dudungtak.seproject.model.exception.BadInputException;
import com.dudungtak.seproject.model.network.Header;
import com.dudungtak.seproject.model.network.Pagination;
import com.dudungtak.seproject.model.network.request.MenuApiRequest;
import com.dudungtak.seproject.model.network.response.MenuApiResponse;
import com.dudungtak.seproject.model.network.response.MenuElementApiResponse;
import com.dudungtak.seproject.domain.repository.DishRepository;
import com.dudungtak.seproject.domain.repository.MenuElementRepository;
import com.dudungtak.seproject.domain.repository.MenuRepository;
import com.dudungtak.seproject.domain.service.BaseCrudApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuApiService {

    private final MenuRepository menuRepository;

    private final MenuElementRepository menuElementRepository;

    private final DishRepository dishRepository;

    @Autowired
    public MenuApiService(MenuRepository menuRepository, MenuElementRepository menuElementRepository, DishRepository dishRepository) {
        this.menuRepository = menuRepository;
        this.menuElementRepository = menuElementRepository;
        this.dishRepository = dishRepository;
    }

    @Transactional
    public Header<MenuApiResponse> create(Header<MenuApiRequest> request) {
        MenuApiRequest body = request.getData();

        // build menu without price
        Menu menu = Menu.builder()
                .name(body.getName())
                .imgUrl(body.getImgUrl())
                .image(body.getImage())
                .registeredAt(body.getRegisteredAt())
                .build();

        // build menuElement without menu
        List<MenuElement> menuElementList = body.getMenuElementList().stream()
                .filter(menuElementApiRequest -> {
                    return menuElementApiRequest.getQuantity() != null;
                })
                .map(menuElementApiRequest -> {
                    Dish dish = dishRepository.getOne(menuElementApiRequest.getDishId());

                    MenuElement menuElement = MenuElement.builder()
                            .totalPrice(dish.getPrice().multiply(BigDecimal.valueOf(menuElementApiRequest.getQuantity())))
                            .quantity(menuElementApiRequest.getQuantity())
                            .dish(dish)
                            .build();

                    return menuElement;
                })
                .collect(Collectors.toList());

        // estimate total price of menu  &  save menu
        BigDecimal totalPrice = menuElementList.stream()
                .map(MenuElement::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println(totalPrice);
        menu.setPrice(totalPrice);
        Menu savedMenu = menuRepository.save(menu);

        // set menu id to menuElements  & save menuElements
        List<MenuElement> savedMenuElementList = menuElementList.stream()
                .map(menuElement -> {
                    menuElement.setMenu(savedMenu);
                    MenuElement savedMenuElement =  menuElementRepository.save(menuElement);

                    return savedMenuElement;
                })
                .collect(Collectors.toList());
        savedMenu.setMenuElementList(savedMenuElementList);         // set menuElements for response

        MenuApiResponse menuApiResponse = response(savedMenu);

        return Header.OK(menuApiResponse);
    }

    public Header<MenuApiResponse> read(Long id) {
        return menuRepository.findById(id)
            .map(MenuApiService::response)
            .map(Header::OK)
            .orElseThrow(BadInputException::new);
    }

    public Header<List<MenuApiResponse>> readAll(Pageable pageable) {
        Page<Menu> menuPages = menuRepository.findAll(pageable);

        List<MenuApiResponse> menuApiResponseList = menuPages.stream()
                .map(MenuApiService::response)
                .collect(Collectors.toList());

        Pagination pagination = BaseCrudApiService.pagination(menuPages);

        return Header.OK(menuApiResponseList, pagination);
    }

    @Transactional
    public Header<MenuApiResponse> update(Header<MenuApiRequest> request) {
        MenuApiRequest body = request.getData();

        Optional<Menu> optionalMenu = menuRepository.findById(body.getId());

        return optionalMenu
                .map(menu -> {
                    // update menu
                    menu.setName(body.getName())
                        .setImgUrl(body.getImgUrl())
                        .setImage(body.getImage())
                        .setRegisteredAt(body.getRegisteredAt())
                        .setUnregisteredAt(body.getUnregisteredAt());

                    // delete old menuElement
                    menuElementRepository.deleteAllByMenu(menu);

                    // create updated menuElements  &  save menuElements
                    List<MenuElement> menuElementList = body.getMenuElementList().stream()
                            .filter(menuElementApiRequest -> {
                                return menuElementApiRequest.getQuantity() != null;
                            })
                            .map(menuElementApiRequest -> {
                                Dish dish = dishRepository.getOne(menuElementApiRequest.getDishId());

                                MenuElement menuElement = MenuElement.builder()
                                    .totalPrice(dish.getPrice().multiply(BigDecimal.valueOf(menuElementApiRequest.getQuantity())))
                                    .quantity(menuElementApiRequest.getQuantity())
                                    .menu(menu)
                                    .dish(dish)
                                    .build();

                                MenuElement savedMenuElement = menuElementRepository.save(menuElement);

                                return savedMenuElement;
                            })
                            .collect(Collectors.toList());

                    // save updated menu
                    BigDecimal totalPrice = menuElementList.stream()
                            .map(MenuElement::getTotalPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    menu.setPrice(totalPrice);
                    Menu savedMenu = menuRepository.save(menu);

                    MenuApiResponse menuApiResponse = MenuApiService.response(savedMenu);

                    return Header.OK(menuApiResponse);
                })
                .orElseThrow(BadInputException::new);
    }

    @Transactional
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
                .orElseThrow(BadInputException::new);
    }

    public static MenuApiResponse response(Menu menu) {
        List<MenuElementApiResponse> menuElementApiResponseList = menu.getMenuElementList().stream()
                    .map(MenuElementApiService::response)
                    .collect(Collectors.toList());

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
