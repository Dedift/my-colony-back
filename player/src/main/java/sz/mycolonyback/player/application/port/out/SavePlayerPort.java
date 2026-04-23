package sz.mycolonyback.player.application.port.out;

import sz.mycolonyback.player.domain.Player;

public interface SavePlayerPort {

	void save(Player player);
}
