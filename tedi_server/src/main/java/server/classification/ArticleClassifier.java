package server.classification;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.entities.ArticleEntity;
import server.repositories.ArticleRepository;
import server.utilities.Distance;

@Component
public class ArticleClassifier extends Classifier<ArticleEntity> {
	
	@Autowired
	private ArticleRepository articleRepo;
	
	public ArticleClassifier() {
		super();
	}
	
	public ArticleClassifier(int k) {
		super(k);
	}
	
	protected int calculateDistance(ArticleEntity a1, ArticleEntity a2) {
		int totalDistance = 0;
		totalDistance += Distance.Levenshtein(a1.getTitle(), a2.getTitle());
		totalDistance += Distance.Levenshtein(a1.getText(), a2.getText());
		return totalDistance;
	}
	
	protected Categories getItemCategory(ArticleEntity article) {
		return article.getCategories();
	}
	
	@PostConstruct
	protected void generateInitialDataset() {
		for (int i=1; i<=3; i++) {
			if (articleRepo.findByTitleAndUserIsNull("Research" + i) == null) {
				ArticleEntity article = new ArticleEntity();
				article.setTitle("Research" + i);
				article.setText("A new research paper reveals previously unknown information. The researchers seem to be unsure about the implications of such a scientific discovery");
				article.setCategories(Categories.RESEARCH);
				articleRepo.save(article);
			}
			if (articleRepo.findByTitleAndUserIsNull("AI" + i) == null) {
				ArticleEntity article = new ArticleEntity();
				article.setTitle("AI" + i);
				article.setText("Neural networks may be our gate to the future. New algorithms improving deep learning and AI can help us predict the future more easily");
				article.setCategories(Categories.AI);
				articleRepo.save(article);
			}
			if (articleRepo.findByTitleAndUserIsNull("Biology" + i) == null) {
				ArticleEntity article = new ArticleEntity();
				article.setTitle("Biology" + i);
				article.setText("A new kind of mitochondria, one of the components of the human cell, is discovered. The information that biologists in a lab found could prove useful to counter cancer and other diseases.");
				article.setCategories(Categories.BIOLOGY);
				articleRepo.save(article);
			}
			if (articleRepo.findByTitleAndUserIsNull("Gossip" + i) == null) {
				ArticleEntity article = new ArticleEntity();
				article.setTitle("Gossip" + i);
				article.setText("The queen and the prince of England photographed together in vacation! Shocking news in Hollywood, as greek singer is invited to sing in the oscar awards ceremony");
				article.setCategories(Categories.GOSSIP);
				articleRepo.save(article);
			}
		}
	}

}
