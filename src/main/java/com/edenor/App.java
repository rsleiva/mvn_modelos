package com.edenor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

class App {

    private static Propiedades p;

	public static void main(String[] args) {
        p= new Propiedades();
        // Crear un temporizador
        Timer timer = new Timer();
        // Crear una tarea
        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                // Ejecutar la tarea
				ModeloX m1 = new ModeloX(2,p);
				m1.obtieneDocumentosNuevos();
                getSchedule();
            }
        };
        // Programar la tarea para que se ejecute cada 5 minutos
        timer.schedule(tarea,0, getSchedule());
	}

    private static Long getSchedule(){
        LocalDateTime fechaHora = LocalDateTime.now();
        /*
         * Seteo para que el segundo sea 0
         */
        do {
            fechaHora = fechaHora.plusSeconds(1);            
        } while (fechaHora.getSecond()!=0);
        /*
         * Seteo para que se ejecue cada 5 min de reloj
         */
        while (fechaHora.getMinute()%5!=0) {
            fechaHora = fechaHora.plusMinutes(1);            
        }
        // Obtener la diferencia en log
        long diferencia = LocalDateTime.now().until(fechaHora, ChronoUnit.MILLIS);
        // Imprimir la fecha/hora sumada
        System.out.println("Proxima corrida: "+fechaHora);
        return diferencia;
    }

}