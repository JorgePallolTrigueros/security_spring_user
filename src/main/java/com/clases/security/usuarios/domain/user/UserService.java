package com.clases.security.usuarios.domain.user;
import com.clases.security.usuarios.dao.entity.MovieEntity;
import com.clases.security.usuarios.dao.entity.MovieUserEntity;
import com.clases.security.usuarios.dao.entity.UserEntity;
import com.clases.security.usuarios.dao.repository.MovieUserRepository;
import com.clases.security.usuarios.dao.repository.AddressRepository;
import com.clases.security.usuarios.dao.repository.UserRepository;
import com.clases.security.usuarios.domain.shared.dto.MovieUserDto;
import com.clases.security.usuarios.domain.shared.dto.UserDto;
import com.clases.security.usuarios.domain.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MovieUserRepository movieUserRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final AddressRepository addressRepository;


    public UserService(AddressRepository addressRepository,
                       MovieUserRepository movieUserRepository,
                       UserRepository userRepository,
                       ModelMapper mapper)
    {
        this.movieUserRepository = movieUserRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.mapper = mapper;
    }


    /**
     * Devuelve la lista de usuarios
     * @return List<UserDto>
     */
    public List<UserDto> findAllUsers(){
        return userRepository
                .findAll() //llamar al repositorio que nos devuelve una lista
                .stream() // con stream lo convierto en un flujo que puedo trabajar
                .map( userEntity -> mapper.map(userEntity,UserDto.class) ) //mapeo cada elemento a UserDto.class
                .collect(Collectors.toList());//recolectar los resultados y devolver una lista
    }

    /**
     * Buscar usuario por id
     * @param id
     * @param model
     * @return
     */
    public String viewUser(Long id, Model model) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if(userEntityOptional.isPresent()){
            //TODO mapear a movies
            List<MovieUserEntity> movieUserEntities = movieUserRepository.findAllByUserId(id);
            List<MovieEntity> movies = movieUserEntities.stream().map(MovieUserEntity::getMovie).collect(Collectors.toList());//si esta presente
            model.addAttribute("user", mapper.map(userEntityOptional.get(), UserDto.class) );
            model.addAttribute("movies",movies);
            //TODO falta DTO
            //MAPEAR LA DIRECCION
            List<AddressDto> addressDtoList = addressRepository
                    .findAllAddressByIdUser(id)
                    .stream()
                    .map(m -> mapper.map(m,AddressDto.class))
                    .collect(Collectors.toList());
            model.addAttribute("addressUsers", addressDtoList );

            return "user-view";
        }else{
            //si no esta presente
            return "index";
        }
    }

    public String deleteUser(Long id, Model model) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if(userEntityOptional.isPresent()){
            //si esta presente
            userRepository.deleteById(id);
            return "index";
        }else{
            //si no esta presente
            //devolver a una pagina de error no se encuentra
            return "index";
        }
    }


    public String editUser(Model model,UserDto userDto) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userDto.getId());

        if(userEntityOptional.isPresent()){
            //si esta presente
            UserEntity  userEntity = userEntityOptional.get();
            //mapear de un objeto ya existente los datos de otro
            mapper.map(userDto,userEntity);
            //guardar
            userEntity = userRepository.saveAndFlush(userEntity);
            //agregar al model attribute
            model.addAttribute("user", mapper.map(userEntity, UserDto.class) );
            //retornar la vista
            return "user-view";
        }else{
            //si no esta presente
            return "index";
        }
    }

    public String saveUser(Model model,UserDto userDto) {

        log.info("user to save: "+userDto.toString());

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(userDto.getUsername());

        //si hay un usuario con el username debe lanzar un error
        if(userEntityOptional.isPresent()){
            model.addAttribute("error","Ya existe el usuario con el mismo username");
            return "error";
        }

        userDto.setStatus("ACTIVO");
        userDto.setCreated(new Date());
        userDto.setRol("VISITOR");
        userDto.setEnabled(true);


        UserEntity userEntity = mapper.map(userDto,UserEntity.class);

        userEntity = userRepository.saveAndFlush(userEntity);
        //agregar al model attribute
        model.addAttribute("user", mapper.map(userEntity, UserDto.class) );
        //retornar la vista

        return "login";

    }




    public String viewUserEdit(Long id, Model model) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(id);

        if(userEntityOptional.isPresent()){
            //si esta presente
            model.addAttribute("user", mapper.map(userEntityOptional.get(), UserDto.class) );
            return "user-edit";
        }else{
            //si no esta presente
            return "index";
        }
    }
}
