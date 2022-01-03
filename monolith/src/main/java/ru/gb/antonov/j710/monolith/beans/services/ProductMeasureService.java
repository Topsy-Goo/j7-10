package ru.gb.antonov.j710.monolith.beans.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.ResourceNotFoundException;
import ru.gb.antonov.j710.monolith.beans.repositos.ProductMeasureRepo;
import ru.gb.antonov.j710.monolith.entities.Measure;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductMeasureService {

    private final ProductMeasureRepo productMeasureRepo;

    public Measure findByName (String measureName) {
        String errMsg = "Еденица измерения товара не найдена: " + measureName;
        return productMeasureRepo.findByName(measureName)
                                 .orElseThrow(()->new ResourceNotFoundException(errMsg));
    }

    public List<String> getMeasuresList () {
        return productMeasureRepo.findAllNames();
    }
}
