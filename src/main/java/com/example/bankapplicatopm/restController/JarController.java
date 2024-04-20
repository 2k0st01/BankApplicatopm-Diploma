package com.example.bankapplicatopm.restController;

import com.example.bankapplicatopm.dto.jar.CreateJarDTO;
import com.example.bankapplicatopm.dto.jar.InfoJarDTO;
import com.example.bankapplicatopm.dto.jar.TransferToJar;
import com.example.bankapplicatopm.dto.transactions.AccountTransactionDTO;
import com.example.bankapplicatopm.dto.transactions.JarTransactionDTO;
import com.example.bankapplicatopm.model.Jar;
import com.example.bankapplicatopm.model.Transaction;
import com.example.bankapplicatopm.service.JarService;
import com.example.bankapplicatopm.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class JarController {
    private final JarService jarService;

    @GetMapping("/jar/show/{id}")
    public InfoJarDTO getJarInformation(@PathVariable("id") long jarId){
        Jar jar = jarService.findJarById(jarId);
        InfoJarDTO infoJarDTO = new InfoJarDTO();
        infoJarDTO.setJarName(jar.getJarName());
        infoJarDTO.setCurrency(jar.getCurrency());
        infoJarDTO.setComment(jar.getComment());
        infoJarDTO.setId(jar.getId());
        infoJarDTO.setCurrentSum(jar.getCurrentSum());
        infoJarDTO.setMaxSize(jar.getMaxSize());

        return infoJarDTO;
    }

    @PostMapping("/jar/add")
    public void addToJar(@RequestBody TransferToJar request){
        jarService.transferMoney(request.getJarId(),request.getSum(),request.getJarCurrency(),request.getComment());
    }

    @PostMapping("/jar/create")
    public void createJar(@RequestBody CreateJarDTO request){
        jarService.createJar(request.getJarName(),request.getMaxSize(),request.getCurrency(),request.getComment());
    }

    @GetMapping("/jar/getTransaction/{id}")
    public List<JarTransactionDTO> getJarTransaction(@PathVariable("id") long jarId){
        List<Transaction> list = jarService.getJarTransaction(jarId);
        List<JarTransactionDTO> main = list.stream()
                .map(t -> {
                    JarTransactionDTO dto = new JarTransactionDTO();
                    dto.setFromAccount(t.getFromAccount());
                    dto.setSum(t.getSum());
                    dto.setDate(t.getDate());
                    dto.setComment(t.getComment());
                    return dto;
                }).collect(Collectors.toList());

        return main;
    }
}
