package br.upf.ads_emilybarriquel_proj_maita.converter;

import br.upf.ads_emilybarriquel_proj_maita.entity.AndarEntity;
import br.upf.ads_emilybarriquel_proj_maita.facade.AndarFacade;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "andarConverter", managed = true)
public class AndarConverter implements Converter<AndarEntity> {

    @EJB
    private AndarFacade andarFacade;

    @Override
    public AndarEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return andarFacade.find(Long.parseLong(value));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, AndarEntity value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return String.valueOf(value.getId());
    }
}
