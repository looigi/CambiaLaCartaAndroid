package com.looigi.cambiolacarta.Soap;

public class DBRemoto {
	private String RadiceWS = "http://looigi.no-ip.biz:97/looVF/";
	private String ws = "looVF.asmx/";
	private String NS="http://looVF.org/";
	private String SA="http://looVF.org/";

    private String ToglieCaratteriStrani(String Cosa) {
		if (Cosa!=null) {
			String sCosa = Cosa.replace("?", "***PI***");
			sCosa = sCosa.replace("&", "***AND***");

			return sCosa;
		} else {
			return "";
		}
	}

	public void TornaNumeroImmaginePerSfondo() {
		String Urletto="TornaNumeroImmaginePerSfondo";

		GestioneWEBServiceSOAP g = new GestioneWEBServiceSOAP(
				RadiceWS + ws + Urletto,
				"TornaNumeroImmaginePerSfondo",
				NS,
				SA,
				5000,
				false);
		g.Esegue();
	}
}
