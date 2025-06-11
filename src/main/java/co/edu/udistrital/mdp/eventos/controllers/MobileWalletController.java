package co.edu.udistrital.mdp.eventos.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.eventos.dto.paymentdto.MethodOfPaymentDTO;
import co.edu.udistrital.mdp.eventos.dto.paymentdto.MobileWalletDTO;
import co.edu.udistrital.mdp.eventos.entities.paymententity.MobileWalletEntity;
import co.edu.udistrital.mdp.eventos.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.eventos.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.eventos.services.MobileWalletService;


@RestController
@RequestMapping("/mobile-wallets")
public class MobileWalletController {
    @Autowired
    private MobileWalletService mobileWalletService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MethodOfPaymentDTO> findAll() {

        List<MobileWalletEntity> mobileWallets = mobileWalletService.getMobileWallets();

        return modelMapper.map(mobileWallets, new TypeToken<List<MethodOfPaymentDTO>>() {
        }.getType());

    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MethodOfPaymentDTO findOne(@PathVariable Long id) throws EntityNotFoundException {

        MobileWalletEntity mobileWalletEntity = mobileWalletService.getMobileWallet(id);

        return modelMapper.map(mobileWalletEntity, MethodOfPaymentDTO.class);

    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MobileWalletDTO create(@RequestBody MobileWalletDTO mobileWalletDTO)
            throws IllegalOperationException, EntityNotFoundException {

        MobileWalletEntity mobileWalletEntity = mobileWalletService
                .createMobileWallet(modelMapper.map(mobileWalletDTO, MobileWalletEntity.class));

        return modelMapper.map(mobileWalletEntity, MobileWalletDTO.class);

    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MobileWalletDTO update(@PathVariable Long id, @RequestBody MobileWalletDTO mobileWalletDTO)
            throws EntityNotFoundException, IllegalOperationException {

        MobileWalletEntity mobileWalletEntity = mobileWalletService.updateMobileWallet(id,
                modelMapper.map(mobileWalletDTO, MobileWalletEntity.class));

        return modelMapper.map(mobileWalletEntity, MobileWalletDTO.class);

    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        mobileWalletService.deleteMobileWallet(id);
    }
}
