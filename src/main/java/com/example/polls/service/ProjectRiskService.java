package com.example.polls.service;

import com.example.polls.exception.AppException;
import com.example.polls.model.project.*;
import com.example.polls.payload.requests.project.ProjectRiskDto;
import com.example.polls.payload.response.project.ProjectRiskSensitivityDto;
import com.example.polls.repository.project.*;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProjectRiskService {

    private final ProjectRiskRepository repository;
    private final RiskDBRepository riskDBRepository;


    @Transactional
    public ProjectRisk create(ProjectRisk risk) {
        return repository.save(risk);
    }

    public ProjectRisk update(ProjectRisk risk, Long id) {
        ProjectRisk oldRisk = get(id);
        risk.setId(oldRisk.getId());
        return repository.save(risk);
    }

    public ProjectRisk get(Long riskId) {
        return repository.findById(riskId).orElseThrow(() -> new NotFoundException("риск не найден"));
    }

    public Map<String, List<Double>> getTable(Long riskId) {
        ProjectRisk risk = get(riskId);
        Map<String, List<Double>> table = new HashMap<>();
        try (BufferedInputStream in = new BufferedInputStream(new URL(risk.getExcel().getUrl()).openStream())) {
            Workbook workbook = new XSSFWorkbook(in);
            for (var sheet : workbook) {
                List<String> names = new ArrayList<>();
                for (var col : sheet) {
                    int i = 0;
                    for (var cell : col) {
                        if (cell.getAddress().getRow() == 0) {
                            names.add(cell.getStringCellValue());
                            table.put(cell.getStringCellValue(), new ArrayList<>());
                        } else {
                            table.get(names.get(i)).add(cell.getNumericCellValue());
                        }
                        i++;
                    }
                }
            }
            List<Double> budget = table.get("бюджет");
            List<Double> losses = table.get("потери");
            List<Double> impact = new ArrayList<>();
            for (int i = 0; i < budget.size(); i++) {
                impact.add(losses.get(i) / budget.get(i));
            }
            table.remove("бюджет");
            table.remove("потери");
            table.put("impact", impact);

            return table;
        } catch (NumberFormatException e) {
            throw new AppException("Некорректный формат файла");
        } catch (MalformedURLException e) {
            throw new AppException("Некорректный формат файла");
        } catch (IOException e) {
            throw new AppException("Некорректный формат файла");
        }
    }

}
