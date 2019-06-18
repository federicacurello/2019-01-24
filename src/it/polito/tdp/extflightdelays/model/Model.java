package it.polito.tdp.extflightdelays.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> grafo;
	ExtFlightDelaysDAO dao;
	List<Collegamenti> coll;
	Map<Integer, Airport> airports;

	public Model() {
		grafo= new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		dao= new ExtFlightDelaysDAO();
		airports= new HashMap<Integer, Airport>();
	}
	
	public void creaGrafo() {
		Graphs.addAllVertices(grafo, dao.loadAllStates() );
		dao.loadAllAirports(airports);
		coll= new LinkedList<Collegamenti>(dao.trovaArchi(airports));
		for(Collegamenti c: coll) {
			Graphs.addEdgeWithVertices(grafo, c.getA1().getState(), c.getA2().getState(), c.getPeso());
		}
		System.out.println("Creato grafo con "+ grafo.vertexSet().size()+" vertici e "
				+ grafo.edgeSet().size()+" archi");
		}

	public List<String> loadAllStates() {
		
		return dao.loadAllStates();
	}

	public String visualizza(String stato1) {
		String s="";
		for ( String stato2: Graphs.successorListOf(grafo, stato1)) {
			DefaultWeightedEdge e= grafo.getEdge(stato1, stato2);
			s+= stato2 +" peso: "+grafo.getEdgeWeight(e)+ "\n";
		}
		return s;
	}
}
