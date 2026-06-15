package br.upf.ads_emilybarriquel_proj_maita.converter;

import br.upf.ads_emilybarriquel_proj_maita.entity.ApartamentoEntity;
import br.upf.ads_emilybarriquel_proj_maita.facade.ApartamentoFacade;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "apartamentoConverter", managed = true)
public class ApartamentoConverter implements Converter<ApartamentoEntity> {

    @EJB
    private ApartamentoFacade apartamentoFacade;

    @Override
    public ApartamentoEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return apartamentoFacade.find(Long.parseLong(value));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ApartamentoEntity value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}
