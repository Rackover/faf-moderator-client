package com.faforever.moderatorclient.mapstruct;

import com.faforever.commons.api.dto.BanInfo;
import com.faforever.moderatorclient.ui.domain.BanInfoFX;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {JavaFXMapper.class, PlayerMapper.class, CycleAvoidingMappingContext.class})
public abstract class BanInfoMapper {
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "banStatus", ignore = true)
    public abstract BanInfoFX map(BanInfo dto);

    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "banStatus", ignore = true)
    public abstract BanInfo map(BanInfoFX fxBean);

    public abstract List<BanInfoFX> mapToFX(List<BanInfo> dtoList);

    public abstract List<BanInfo> mapToDto(List<BanInfoFX> fxBeanList);
}
