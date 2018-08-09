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
		totalDistance -= Distance.CommonWords(a1.getTitle(), a2.getTitle());
		totalDistance -= Distance.CommonWords(a1.getText(), a2.getText());
		return totalDistance;
	}
	
	protected Categories getItemCategory(ArticleEntity article) {
		return article.getCategories();
	}
	
	@PostConstruct
	protected void generateInitialDataset() {
		for (int i=1; i<=10; i++) {
			if (articleRepo.findByTitleAndUserIsNull("Research " + i) == null) {
				ArticleEntity article = new ArticleEntity();
				article.setTitle("Research " + i);
				article.setText(String.join(" ", Dataset.getRandomWords(Dataset.researchDataset, 20)));
				article.setCategories(Categories.RESEARCH);
				articleRepo.save(article);
			}
			if (articleRepo.findByTitleAndUserIsNull("AI " + i) == null) {
				ArticleEntity article = new ArticleEntity();
				article.setTitle("AI " + i);
				article.setText(String.join(" ", Dataset.getRandomWords(Dataset.aiDataset, 20)));
				article.setCategories(Categories.AI);
				articleRepo.save(article);
			}
			if (articleRepo.findByTitleAndUserIsNull("Biology " + i) == null) {
				ArticleEntity article = new ArticleEntity();
				article.setTitle("Biology " + i);
				article.setText(String.join(" ", Dataset.getRandomWords(Dataset.biologyDataset, 20)));
				article.setCategories(Categories.BIOLOGY);
				articleRepo.save(article);
			}
			if (articleRepo.findByTitleAndUserIsNull("Gossip " + i) == null) {
				ArticleEntity article = new ArticleEntity();
				article.setTitle("Gossip " + i);
				article.setText(String.join(" ", Dataset.getRandomWords(Dataset.gossipDataset, 20)));
				article.setCategories(Categories.GOSSIP);
				articleRepo.save(article);
			}
		}
	}

}
