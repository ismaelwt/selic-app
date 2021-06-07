package com.challenge.selic.core.calculo.repository;

import com.challenge.selic.util.Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.Gson;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.challenge.selic.util.Utils.formatDate;

@Component
public class SelicTableClient {

    public static Set<SelicObject> fetch() {
        RestTemplate template = new RestTemplate();
        return Set.of(new Gson().fromJson( template.getForObject("https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados?formato=json", String.class), SelicObject[].class));
    }

    @Getter
    @Builder
    @JsonDeserialize(builder = SelicObject.SelicObjectBuilder.class)
    public static class SelicObject {
        private String data;
        private String valor;

        public String getData() {
            return formatDate(this.data, "dd/mm/yyyy", "yyyy-mm-dd");
        }
    }
}
