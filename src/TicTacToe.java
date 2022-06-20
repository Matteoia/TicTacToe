import java.io.*;
import java.util.*;

public class TicTacToe {
	private final int RIGHE = 3;
	private final int COLONNE = 3;
	private final String path = "src/SalvataggiRN/save.bin";
	private Tavolo tavolo;
	private Scanner sc;
	private Random r;
	private List<Tavolo> configurazioniTotali;
	private List<Integer> configurazioniUsate;
	private int vincitore, x, y;

	public TicTacToe() {
		r = new Random();
		tavolo = new Tavolo(new int[RIGHE][COLONNE]);
		sc = new Scanner(System.in);
		configurazioniTotali = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	public void start() {
		try {
			int x, y;
			boolean finito = false;
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
			Object obj = ois.readObject();
			if(obj != null)
				configurazioniTotali =  (ArrayList<Tavolo>)obj;
			ois.close();
			
			while(!finito) {
				configurazioniUsate = new ArrayList<>();
				while(!partitaTerminata()) {
					int conf = getIdConfigurazione();
					configurazioniUsate.add(conf);
					do{
						System.out.println("Dove vuoi inserire la X? Formato: x,y");
						String tmp = sc.nextLine();
						x = Integer.parseInt(tmp.split(",")[0]);
						y = Integer.parseInt(tmp.split(",")[1]);
					}while(tavolo.getStato(x, y) != 0);
					tavolo.posiziona(x, y, 1);
					stampaTavolo();
					gioca(false);
				}
				aggiornaProb();
				if(vincitore == 0)
					System.out.println("Pareggio");
				if(vincitore == 1)
					System.out.println("Hai vinto");
				if(vincitore == 2)
					System.out.println("Hai perso");
				System.out.println("Vuoi giocare ancora? y/n");
				if(sc.nextLine().equalsIgnoreCase("n")) {
					finito = true;
					save();
				}
				else 
					azzeraTavolo();
			}	
		}catch(Exception e) {
			System.err.println("Errore nella lettura del file");
		}
	}
	
	private void gioca(boolean allenamento) {
		int conf = getIdConfigurazione();
		configurazioniUsate.add(conf);
		if(!partitaTerminata()) {
			Tavolo corrente = configurazioniTotali.get(conf);
			do {
				int pos = r.nextInt(corrente.getProb().size());
				x = corrente.getProb().get(pos).getX();
				y = corrente.getProb().get(pos).getY();
			}while(tavolo.getStato(x, y) != 0);
			tavolo.posiziona(x, y, 2);
			if(!allenamento)
				stampaTavolo();
		}
	}
	
	public void start(int volte) throws Exception{
		int x, y;
		for(int i=0; i<volte; i++) {
			configurazioniUsate = new ArrayList<>();
			while(!partitaTerminata()) {
				int conf = getIdConfigurazione();
				configurazioniUsate.add(conf);
				do{
					x = r.nextInt(RIGHE);
					y = r.nextInt(COLONNE);
				}while(tavolo.getStato(x, y) != 0);
				tavolo.posiziona(x, y, 1);
				gioca(true);
			}
			aggiornaProb();
			azzeraTavolo();
		}
		save();
	}


	public void save() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
			oos.writeObject(configurazioniTotali);
			oos.close();
		}catch(Exception e) {
			System.err.print("Errore durante il salvataggio");
		}
	}
	
	private int getIdConfigurazione() {
		for(int k=0; k<configurazioniTotali.size(); k++) {
			if(configurazioniTotali.get(k).equals(tavolo))
				return k;
		}
		configurazioniTotali.add(new Tavolo(tavolo.getMatrice()));
		return configurazioniTotali.size()-1;
	}
	
	private boolean partitaTerminata() {
		this.vincitore = tavolo.getVincitore();
		return (vincitore>-1)?true:false;
	}
	
	private void aggiornaProb() {
		for(int i=1; i<configurazioniUsate.size(); i++) {
			Tavolo tavoloPre = configurazioniTotali.get(configurazioniUsate.get(i-1));
			Tavolo tavoloCorr = configurazioniTotali.get(configurazioniUsate.get(i));
			Coordinate c = Tavolo.getCoordinateMossa(tavoloPre, tavoloCorr);
			if(vincitore == 0)
				pareggioProb(c, configurazioniUsate.get(i-1));
			if(vincitore == 1)
				perdenteProb(c, configurazioniUsate.get(i-1));
			if(vincitore == 2)
				vincitoreProb(c, configurazioniUsate.get(i-1));	
		}	
	}
	
	private void perdenteProb(Coordinate c, int k) {
		if(Collections.frequency(configurazioniTotali.get(k).getProb(), c) > 1)
			configurazioniTotali.get(k).getProb().remove(c);
	}
	
	private void vincitoreProb(Coordinate c, int k) {
		for(int i=0; i<2; i++)
			configurazioniTotali.get(k).getProb().add(c);
	}
	
	private void pareggioProb(Coordinate c, int k) {
		configurazioniTotali.get(k).getProb().add(c);
	}
	
	private void azzeraTavolo() {
		for(int i=0; i<RIGHE; i++) 
			for(int j=0; j<COLONNE; j++) 
				tavolo.posiziona(i, j, 0);
	}
	
	private void stampaTavolo() {
		for(int i=0; i<RIGHE; i++) {
			for(int j=0; j<COLONNE; j++) {
				if(tavolo.getStato(i, j) == 0)
					System.out.print("- ");
				if(tavolo.getStato(i, j) == 1)
					System.out.print("X ");
				if(tavolo.getStato(i, j) == 2)
					System.out.print("O ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	
	public static void main(String[] args) throws Exception{
		TicTacToe t = new TicTacToe();
		t.start();
	}
}
