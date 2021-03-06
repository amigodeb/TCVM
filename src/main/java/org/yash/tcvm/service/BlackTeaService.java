package org.yash.tcvm.service;

import java.util.function.IntPredicate;
import java.util.logging.Logger;

import org.yash.tcvm.container.handler.ContainerManager;
import org.yash.tcvm.enums.BlackTea;
import org.yash.tcvm.enums.DrinksType;
import org.yash.tcvm.exception.MaterialNotSufficientException;
import org.yash.tcvm.service.interfaces.DrinkService;
import org.yash.tcvm.utils.VendingMachineUtils;

public class BlackTeaService implements DrinkService {

	private Logger logger = Logger.getLogger(TeaService.class.getName());
	private ContainerManager containerManager = ContainerManager.getInstance();

	@Override
	public void makeDrink(String drinkType, Integer quantity) {

		if (isMaterialSufficient(quantity)) {

			containerManager.setTeaCapacity(containerManager.getTeaCapacity()
					- BlackTea.TEA.getConsumptionAndWasteMaterialTotal() * quantity);
			containerManager.setSugarCapacity(containerManager.getSugarCapacity()
					- BlackTea.SUGAR.getConsumptionAndWasteMaterialTotal() * quantity);
			containerManager.setWaterCapacity(containerManager.getWaterCapacity()
					- BlackTea.WATER.getConsumptionAndWasteMaterialTotal() * quantity);

			VendingMachineUtils.drinksSales(drinkType, quantity);

			logger.info("Enjoy your drink! You buy " + quantity + " cup of " + drinkType + ", Total cost is "
					+ DrinksType.BLACK_TEA.getPrice() * quantity + ".");

		} else {
			throw new MaterialNotSufficientException("Material not sufficient in container.");
		}

	}

	public boolean isMaterialSufficient(Integer quantity) {

		IntPredicate hasTea = (
				q) -> containerManager.getTeaCapacity() >= BlackTea.TEA.getConsumptionAndWasteMaterialTotal() * q;
		IntPredicate hasWater = (
				q) -> containerManager.getWaterCapacity() >= BlackTea.WATER.getConsumptionAndWasteMaterialTotal() * q;
		IntPredicate hasSugar = (
				q) -> containerManager.getSugarCapacity() >= BlackTea.SUGAR.getConsumptionAndWasteMaterialTotal() * q;

		if (hasTea.and(hasWater).and(hasSugar).test(quantity))
			return true;
		else
			return false;

	}

}
