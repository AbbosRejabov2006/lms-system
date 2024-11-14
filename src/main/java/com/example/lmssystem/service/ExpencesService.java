package com.example.lmssystem.service;

import com.example.lmssystem.entity.Expences;
import com.example.lmssystem.entity.Category;
import com.example.lmssystem.entity.User;
import com.example.lmssystem.repository.ExpencesRepository;
import com.example.lmssystem.repository.CategoryRepository;
import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.transfer.ExpencesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExpencesService {

    @Autowired
    private ExpencesRepository expencesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ExpencesDTO saveExpences(ExpencesDTO expencesDTO) {
        validateExpenseDTO(expencesDTO);
        Optional<User> user = userRepository.findById(expencesDTO.userId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + expencesDTO.userId());
        }
        Optional<Category> category = categoryRepository.findById(expencesDTO.categoryId());
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category not found with id: " + expencesDTO.categoryId());
        }
        Expences expences = new Expences(
                null,
                expencesDTO.Name(),
                expencesDTO.date(),
                category.get(),
                user.get(),
                expencesDTO.amount()
        );
        Expences savedExpences = expencesRepository.save(expences);
        return mapToDTO(savedExpences);
    }

    public List<ExpencesDTO> getAllExpences() {
        List<Expences> expencesList = expencesRepository.findAll();
        return expencesList.stream().map(this::mapToDTO).toList();
    }

    public ExpencesDTO getExpencesById(Long id) {
        Expences expences = expencesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with id: " + id));
        return mapToDTO(expences);
    }

    public ExpencesDTO updateExpences(Long id, ExpencesDTO expencesDTO) {
        validateExpenseDTO(expencesDTO);
        Expences expences = expencesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with id: " + id));
        Optional<User> user = userRepository.findById(expencesDTO.userId());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + expencesDTO.userId());
        }
        Optional<Category> category = categoryRepository.findById(expencesDTO.categoryId());
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category not found with id: " + expencesDTO.categoryId());
        }
        expences.setName(expencesDTO.Name());
        expences.setDate(expencesDTO.date());
        expences.setAmount(expencesDTO.amount());
        expences.setReceiver(user.get());
        expences.setCategory(category.get());

        Expences updatedExpences = expencesRepository.save(expences);
        return mapToDTO(updatedExpences);
    }

    public boolean deleteExpences(Long id) {
        Optional<Expences> expences = expencesRepository.findById(id);
        if (expences.isEmpty()) {
            throw new IllegalArgumentException("Expense not found with id: " + id);
        }

        expencesRepository.delete(expences.get());
        return true;
    }

    private ExpencesDTO mapToDTO(Expences expences) {
        return new ExpencesDTO(
                expences.getName(),
                expences.getDate(),
                expences.getCategory().getId(),
                expences.getReceiver().getId(),
                expences.getAmount()
        );
    }

    private void validateExpenseDTO(ExpencesDTO expencesDTO) {
        if (expencesDTO.Name() == null || expencesDTO.Name().trim().isEmpty()) {
            throw new IllegalArgumentException("Expense name cannot be empty");
        }
        if (expencesDTO.date() == null) {
            throw new IllegalArgumentException("Expense date cannot be null");
        }
        if (expencesDTO.amount() == null || expencesDTO.amount() < 0) {
            throw new IllegalArgumentException("Amount must be a positive value");
        }
        if (expencesDTO.userId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (expencesDTO.categoryId() == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        if (expencesDTO.date().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Expense date cannot be in the future");
        }
    }
}
