package com.example.lmssystem.service;

import com.example.lmssystem.entity.Group;
import com.example.lmssystem.entity.Invoice;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.repository.GroupRepository;
import com.example.lmssystem.repository.InvoiceRepository;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.transfer.InvoiceDTO;
import com.example.lmssystem.transfer.ResponseData;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final GroupRepository groupRepository;
    private  final UserRepository userRepository;

    public InvoiceDTO saveInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = convertToEntity(invoiceDTO);
        invoice = invoiceRepository.save(invoice);
        return convertToDTO(invoice);
    }


    public List<InvoiceDTO> getAllInvoices() {
        return invoiceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));
        return convertToDTO(invoice);
    }

    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));

        invoice.setDate(invoiceDTO.date());
        invoice.setAmount(invoiceDTO.amount());
        invoice.setStatus(invoiceDTO.status());
        invoice.setUser(userRepository.findById(invoiceDTO.userId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + invoiceDTO.userId())));
        invoice.setGroup(groupRepository.findById(invoiceDTO.groupId())
                .orElseThrow(() -> new EntityNotFoundException("Group not found with id: " + invoiceDTO.groupId())));

        invoice = invoiceRepository.save(invoice);
        return convertToDTO(invoice);
    }



    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new EntityNotFoundException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }

    private InvoiceDTO convertToDTO(Invoice invoice) {
        return new InvoiceDTO(invoice.getDate(),invoice.getAmount(),invoice.getUser().getId(),invoice.getGroup().getId(),invoice.getStatus());
    }

    private Invoice convertToEntity(InvoiceDTO invoiceDTO) {
        Optional<User> byId = userRepository.findById(invoiceDTO.userId());
        if (byId.isEmpty()) {
            throw new EntityNotFoundException("User not found with id: " + invoiceDTO.userId());
        }
        Optional<Group> byId1 = groupRepository.findById(invoiceDTO.groupId());
        if (byId1.isEmpty()) {
            throw new EntityNotFoundException("Group not found with id: " + invoiceDTO.groupId());
        }
        return Invoice.builder()
                .date(invoiceDTO.date())
                .amount(invoiceDTO.amount())
                .status(invoiceDTO.status())
                .user(byId.get())
                .group(byId1.get())
                .build();
    }
}
