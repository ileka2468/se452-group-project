package edu.depaul.cdm.se452.rfa.profileManagement.service;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
public class CharacteristicDataWithOptions<Type> extends CharacteristicData<Type> {
    public CharacteristicDataWithOptions(Type value, CharacteristicType type, String[] options) {
        super(value, type);
        if (options == null || options.length == 0) {
            throw new InvalidCharacteristicException("No options provided");
        }
        boolean validOption = Arrays.stream(options).anyMatch(option -> option.equals(value));

        if (!validOption) {
            throw new InvalidCharacteristicException("Invalid option provided");
        }

        this.options = options;
    }
    private String[] options;
}
