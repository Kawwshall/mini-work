package com.tl.reap_admin_api.mapper;

import com.tl.reap_admin_api.dto.StateDto;
import com.tl.reap_admin_api.model.State;
import org.springframework.stereotype.Component;

@Component
public class StateMapper {
    public StateDto toDTO(State state) {
        if (state == null) {
            return null;
        }
        return new StateDto(
            state.getExtId(),
            state.getName(),
            state.getLanguageCode()
        );
    }

    public State toEntity(StateDto dto) {
        if (dto == null) {
            return null;
        }
        State state = new State();
        state.setExtId(dto.getExtId());
        state.setName(dto.getName());
        state.setLanguageCode(dto.getLanguageCode());
        return state;
    }

    public void updateEntityFromDTO(StateDto dto, State state) {
        if (dto == null || state == null) {
            return;
        }
        state.setName(dto.getName());
        state.setLanguageCode(dto.getLanguageCode());
    }
}