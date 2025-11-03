package es.tecnalia.ittxartela.ws.server.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(AccessLevel.PRIVATE)
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setSkipNullEnabled(true);

        // Mapeo de SolicitudTransmision -> TransmisionDatos
        TypeMap<
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.SolicitudTransmision,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos
        > transmisionMap = modelMapper.createTypeMap(
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.SolicitudTransmision.class,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos.class
        );

        transmisionMap.addMappings(mapper -> {
            mapper.skip(es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos::setDatosEspecificos);
        });

        transmisionMap.setPostConverter(context -> {
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.SolicitudTransmision source = context.getSource();
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos destination = context.getDestination();

            if (source.getDatosGenericos() != null) {
                es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.DatosGenericos datosGenericosRespuesta =
                    modelMapper.map(
                        source.getDatosGenericos(),
                        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.DatosGenericos.class
                    );
                destination.setDatosGenericos(datosGenericosRespuesta);
            }

            return destination;
        });

        // Mapeo de Peticion -> Respuesta
        TypeMap<
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Respuesta
        > peticionMap = modelMapper.createTypeMap(
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion.class,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Respuesta.class
        );

        peticionMap.setPostConverter(context -> {
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Peticion source = context.getSource();
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Respuesta destination = context.getDestination();

            if (source.getSolicitudes() != null && source.getSolicitudes().getSolicitudTransmision() != null) {
                List<es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos> transmisiones =
                    source.getSolicitudes().getSolicitudTransmision().stream()
                        .map(solicitud -> modelMapper.map(
                            solicitud,
                            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.TransmisionDatos.class
                        ))
                        .collect(Collectors.toList());

                es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Transmisiones transmisionesWrapper =
                    new es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Transmisiones();
                transmisionesWrapper.getTransmisionDatos().addAll(transmisiones);
                destination.setTransmisiones(transmisionesWrapper);
            }

            if (source.getAtributos() != null) {
                es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos atributos =
                    modelMapper.map(
                        source.getAtributos(),
                        es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Atributos.class
                    );
                destination.setAtributos(atributos);
            }

            return destination;
        });

        // Mapeos internos de DatosGenericos y subcomponentes
        modelMapper.createTypeMap(
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Emisor.class,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Emisor.class
        );

        modelMapper.createTypeMap(
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Solicitante.class,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Solicitante.class
        );

        modelMapper.createTypeMap(
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Funcionario.class,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Funcionario.class
        );

        modelMapper.createTypeMap(
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Procedimiento.class,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Procedimiento.class
        );

        modelMapper.createTypeMap(
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Titular.class,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Titular.class
        );

        modelMapper.createTypeMap(
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.Transmision.class,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.Transmision.class
        );

        modelMapper.createTypeMap(
            es.redsara.intermediacion.scsp.esquemas.v3.online.peticion.DatosGenericos.class,
            es.redsara.intermediacion.scsp.esquemas.v3.online.respuesta.DatosGenericos.class
        );

        return modelMapper;
    }
}
