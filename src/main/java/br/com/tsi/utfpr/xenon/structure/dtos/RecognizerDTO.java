package br.com.tsi.utfpr.xenon.structure.dtos;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RecognizerDTO {

    private Boolean identified;

    private Boolean authorize;

    private Driver driver;

    private Float tax;

    public static RecognizerDTO getNewInstance(CarResultDTO car, Float tax) {
        var recognizerDTO = new RecognizerDTO();
        recognizerDTO.setTax(tax);

        if (car != null) {
            var driver = buildDriver(car);

            adjustmentLastAccess(car, driver);
            setInfoDriver(car, driver);

            setRecognizer(car, recognizerDTO, driver);
        } else {
            recognizerDTO.setIdentified(false);
        }

        return recognizerDTO;
    }

    private static void setRecognizer(CarResultDTO car, RecognizerDTO recognizerDTO, Driver driver) {
        recognizerDTO.setDriver(driver);
        recognizerDTO.setAuthorize(car.getUser().getAuthorizedAccess());
        recognizerDTO.setIdentified(true);
    }

    private static void setInfoDriver(CarResultDTO car, Driver driver) {
        driver.userName = car.getUser().getName();
        driver.userAvatar = "/user/avatar/" + driver.userId.toString();
        driver.type = car.getUser().getTypeUser().name();
        driver.authorizedAccess = car.getUser().getAuthorizedAccess();
        driver.accessNumber = car.getUser().getAccessNumber();
    }

    private static void adjustmentLastAccess(CarResultDTO car, Driver driver) {
        var lastAccess =
            car.getLastAccess() == null ? LocalDateTime.now() : car.getLastAccess();
        driver.lastAccess = String
            .format("%d/%d/%d", lastAccess.getDayOfMonth(), lastAccess.getMonthValue() + 1,
                lastAccess.getYear());
        driver.lastHoursAccess =
            String.format("%d : %d", lastAccess.getHour(), lastAccess.getMinute());
    }

    private static Driver buildDriver(CarResultDTO car) {
        var driver = new Driver();
        driver.userId = car.getUser().getId();
        driver.carModel = car.getModel();
        driver.plate = car.getPlate();
        return driver;
    }

    @Data
    public static class Driver {

        private Long userId;
        private String userName;
        private String type;
        private String userAvatar;
        private String lastAccess;
        private String lastHoursAccess;
        private String carModel;
        private String plate;
        private Boolean authorizedAccess;
        private Integer accessNumber;
    }
}
