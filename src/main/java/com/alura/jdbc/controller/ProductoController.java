package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;
import com.sun.source.tree.StatementTree;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public void modificar(String nombre, String descripcion, Integer id) {
		// TODO
	}

	public void eliminar(Integer id) {
		// TODO
	}

	public List<Map<String, String>> listar() throws SQLException {
		Connection conex = new ConnectionFactory().recuperaConexion();
		Statement statement = conex.createStatement();

		boolean execute = statement.execute("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");

		ResultSet resultSet = statement.getResultSet();
		List<Map<String,String>> resultado = new ArrayList<>();

		while (resultSet.next()) {
			Map<String,String> fila = new HashMap<>();
			fila.put("ID", String.valueOf(resultSet.getInt("ID")));
			fila.put("NOMBRE", resultSet.getString("NOMBRE"));
			fila.put("DESCRIPCION", resultSet.getString("NOMBRE"));
			fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));
			resultado.add(fila);
		}
		conex.close();
		return resultado;
	}

	public void guardar(Map<String, String> producto) throws SQLException {
		Connection conex = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			conex = new ConnectionFactory().recuperaConexion();
			String query = "INSERT INTO PRODUCTO (nombre, descripcion, cantidad) VALUES (?, ?, ?)";
			preparedStatement = conex.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, producto.get("NOMBRE"));
			preparedStatement.setString(2, producto.get("DESCRIPCION"));
			preparedStatement.setInt(3, Integer.parseInt(producto.get("CANTIDAD")));

			int affectedRows = preparedStatement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("La inserción no tuvo éxito.");
			}

			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				System.out.println(String.format("Fue insertado el producto de ID %d", resultSet.getInt(1)));
			} else {
				throw new SQLException("No se pudo obtener el ID generado.");
			}
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (conex != null) {
				conex.close();
			}
		}
	}

}
