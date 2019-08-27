/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.loja;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.isEmptyString;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;
import com.redhat.loja.repository.ProdutoRepository;
import com.redhat.loja_online.Produto;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LojaOnLineApplicationIT {

	private static final String PRODUTOS_PATH = "api/produtos";

	@Value("${local.server.port}")
	private int port;

	@Autowired
	private ProdutoRepository repository;

	@Before
	public void beforeTest() {
		repository.deleteAll();
		RestAssured.baseURI = String.format("http://localhost:%d/" + PRODUTOS_PATH, port);
	}

	@Test
	public void testGetAll() {
		Produto p1 = Produto.builder().codigo("ABC1234").descricao("Camisa Red Hat").valorUnitario(50.0).build();
		repository.save(p1);
		Produto p2 = Produto.builder().codigo("XYZ6789").descricao("Chapeu Red Hat").valorUnitario(75.0).build();
		repository.save(p2);
		requestSpecification().get().then().statusCode(200)
				.body("id", hasItems(p1.getId().intValue(), p2.getId().intValue()))
				.body("descricao", hasItems("Camisa Red Hat", "Chapeu Red Hat"));
	}

	@Test
	public void testGetEmptyArray() {
		requestSpecification().get().then().statusCode(200).body(is("[]"));
	}

	@Test
	public void testGetOne() {
		Produto produto = Produto.builder().codigo("ABC1234").descricao("Camisa Red Hat").valorUnitario(50.0).build();
		repository.save(produto);
		requestSpecification().get(String.valueOf(produto.getId())).then().statusCode(200)
				.body("id", is(produto.getId().intValue())).body("descricao", is(produto.getDescricao()));
	}

	@Test
	public void testGetNotExisting() {
		requestSpecification().get("0").then().statusCode(404);
	}

	@Test
	public void testPost() {
		Produto produto = Produto.builder().codigo("ABC1234").descricao("Camisa Red Hat").valorUnitario(50.0).build();
		produto.setDescricao("Camiseta LOL");
		requestSpecification().contentType(ContentType.JSON).body(new Gson().toJson(produto)).post().then()
				.statusCode(201).body("id", not(isEmptyString())).body("descricao", is("Camiseta LOL"));
	}

	@Test
	public void testPostWithWrongPayload() {
		requestSpecification().contentType(ContentType.JSON).body(Collections.singletonMap("id", 0)).when().post()
				.then().statusCode(422);
	}


    @Test
    public void testPostWithNonJsonPayload() {
        requestSpecification()
                .contentType(ContentType.XML)
                .when()
                .post()
                .then()
                .statusCode(415);
    }
//
//    @Test
//    public void testPostWithEmptyPayload() {
//        requestSpecification()
//                .contentType(ContentType.JSON)
//                .when()
//                .post()
//                .then()
//                .statusCode(415);
//    }
//
//    @Test
//    public void testPut() {
//        Fruit apple = fruitRepository.save(new Fruit("Apple"));
//        requestSpecification()
//                .contentType(ContentType.JSON)
//                .body(Collections.singletonMap("name", "Lemon"))
//                .when()
//                .put(String.valueOf(apple.getId()))
//                .then()
//                .statusCode(200)
//                .body("id", is(apple.getId()))
//                .body("name", is("Lemon"));
//
//    }
//
//    @Test
//    public void testPutNotExisting() {
//        requestSpecification()
//                .contentType(ContentType.JSON)
//                .body(Collections.singletonMap("name", "Lemon"))
//                .when()
//                .put("/0")
//                .then()
//                .statusCode(404);
//    }
//
//    @Test
//    public void testPutWithWrongPayload() {
//        Fruit apple = fruitRepository.save(new Fruit("Apple"));
//        requestSpecification()
//                .contentType(ContentType.JSON)
//                .body(Collections.singletonMap("id", 0))
//                .when()
//                .put(String.valueOf(apple.getId()))
//                .then()
//                .statusCode(422);
//    }
//
//    @Test
//    public void testPutWithNonJsonPayload() {
//        Fruit apple = fruitRepository.save(new Fruit("Apple"));
//        requestSpecification()
//                .contentType(ContentType.XML)
//                .when()
//                .put(String.valueOf(apple.getId()))
//                .then()
//                .statusCode(415);
//    }
//
//    @Test
//    public void testPutWithEmptyPayload() {
//        Fruit apple = fruitRepository.save(new Fruit("Apple"));
//        requestSpecification()
//                .contentType(ContentType.JSON)
//                .when()
//                .put(String.valueOf(apple.getId()))
//                .then()
//                .statusCode(415);
//    }
//
//    @Test
//    public void testDelete() {
//        Fruit apple = fruitRepository.save(new Fruit("Apple"));
//        requestSpecification()
//                .delete(String.valueOf(apple.getId()))
//                .then()
//                .statusCode(204);
//        assertFalse(fruitRepository.exists(apple.getId()));
//    }
//
//    @Test
//    public void testDeleteNotExisting() {
//        requestSpecification()
//                .delete("/0")
//                .then()
//                .statusCode(404);
//    }
//
//
	private RequestSpecification requestSpecification() {
		return given().baseUri(String.format("http://localhost:%d/%s", port, PRODUTOS_PATH));
	}
}
