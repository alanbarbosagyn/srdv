package br.com.ac.srdv.util;

import java.text.DecimalFormat;

public class Funcoes {
	public static String formatarDouble(double valor) {
		DecimalFormat formatador = new DecimalFormat("##,##00.00");
		String s = formatador.format(valor);
		s = s.replace('.', ',');

		return s;
	}
}
