package com.microservicios.exam.clientes.controller;

import com.google.gson.Gson;
import com.microservicios.exam.clientes.entity.Cliente;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ClienteController {

    final AtomicLong counter = new AtomicLong();

    @PostMapping("/cliente/alta")
    public Cliente create(@RequestBody Cliente cliente){
        cliente.setId(counter.incrementAndGet());
        generarJSON(cliente);
        return cliente;
    }

    @PutMapping("/cliente/actualiza")
    public Cliente update(@RequestBody Cliente cliente){
        Cliente existente = new Cliente();
        existente = leerJSON();
        existente.setNombre(cliente.getNombre());
        existente.setCorreo(cliente.getCorreo());
        generarJSON(existente);

        return existente;
    }

    @GetMapping(value = "/cliente/get")
    public Cliente cliente(){
        Cliente cliente = leerJSON();
        return cliente;
    }

    @DeleteMapping("/cliente/elimina")
    public void delete(){
        eliminaJSON();

    }


    public void generarJSON(Cliente cliente){

        try(FileWriter file = new FileWriter("clientes.json")){
            JSONObject clienteJSON = new JSONObject();
            clienteJSON.put("id",cliente.getId());
            clienteJSON.put("nombre",cliente.getNombre());
            clienteJSON.put("correo",cliente.getCorreo());
            file.write(clienteJSON.toString());
            file.flush();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Cliente leerJSON() {

        Gson gson = new Gson();
        String fichero = "";

        try (BufferedReader br = new BufferedReader(new FileReader("clientes.json"))) {
            String linea;
            linea = br.readLine();
            fichero += linea;

        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        Properties properties = gson.fromJson(fichero, Properties.class);
        Cliente cliente  = gson.fromJson(fichero, Cliente.class);
        return cliente;
    }

    public void eliminaJSON(){

        try(FileWriter file = new FileWriter("clientes.json")){
            JSONObject clienteJSON = new JSONObject();
            clienteJSON.remove("nombre");
            clienteJSON.remove("correo");
            clienteJSON.remove("id");
            file.write(clienteJSON.toString());
            file.flush();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
