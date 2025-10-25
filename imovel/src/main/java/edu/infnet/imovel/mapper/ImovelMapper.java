package edu.infnet.imovel.mapper;

import edu.infnet.imovel.model.Imovel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import edu.infnet.imovel.dto.ImovelCreateDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImovelMapper {
    Imovel toEntity(ImovelCreateDto dto);
}
