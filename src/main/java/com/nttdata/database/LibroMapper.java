package com.nttdata.database;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.nttdata.model.Autore;
import com.nttdata.model.Libro;


@Mapper
public interface LibroMapper {

	@Select("select * from Libro where idLibro = #{idLibro}")
	Libro findByIdLibro(@Param("idLibro") int idLibro);

	@Insert("insert into Libro (titolo, genere, prezzo, scaffale) values (#{titolo}, #{genere}, #{prezzo}, #{scaffale})")
	@Options(useGeneratedKeys = true, keyProperty = "idLibro")
	int add(Libro libro);

	@Delete("delete Libro where idLibro = #{idLibro}")
	int delete(@Param("idLibro") int idLibro);

	@Update("update Libro set titolo = #{titolo},"
				+ " genere = #{genere},"
				+ " prezzo = #{prezzo},"
				+ " scaffale = #{scaffale}"
				+ " where idLibro = #{idLibro}")
	int update(Libro libro);

	@Select ("<script> select * from Libro"
			+"<where>"
			+"<if test='genere != null'> AND genere=#{genere}</if>"
			+"<if test='titolo != null'> AND titolo=#{titolo}</if>"
			+"</where>"
			+"</script>")
	List<Libro> findAll (Libro libro);
	
	@Select ("select l.* from libro l, autore a, AutoreLibro al where a.nome=#{nome} and a.cognome=#{cognome} and l.idLibro=al.idLibro and al.idAutore=a.idAutore")			
	List<Libro> findNomeCognome (Autore autore);
	

}
