package dax_api.shared.helpers.magic;

import dax_api.shared.Pair;
import net.runelite.api.Skill;
import net.runelite.rsb.internal.globval.enums.MagicBook;
import net.runelite.rsb.methods.Web;


public enum  Spell implements Validatable  {

    VARROCK_TELEPORT    (SpellBook.Type.STANDARD, 25, MagicBook.Standard.VARROCK_TELEPORT, new Pair<>(1, RuneElement.LAW), new Pair<>(3, RuneElement.AIR),     new Pair<>(1, RuneElement.FIRE)),
    LUMBRIDGE_TELEPORT  (SpellBook.Type.STANDARD, 31, MagicBook.Standard.LUMBRIDGE_TELEPORT, new Pair<>(1, RuneElement.LAW), new Pair<>(3, RuneElement.AIR),     new Pair<>(1, RuneElement.EARTH)),
    FALADOR_TELEPORT    (SpellBook.Type.STANDARD, 37, MagicBook.Standard.FALADOR_TELEPORT, new Pair<>(1, RuneElement.LAW), new Pair<>(3, RuneElement.AIR),     new Pair<>(1, RuneElement.WATER)),
    CAMELOT_TELEPORT    (SpellBook.Type.STANDARD, 45, MagicBook.Standard.CAMELOT_TELEPORT, new Pair<>(1, RuneElement.LAW), new Pair<>(5, RuneElement.AIR)),
    ARDOUGNE_TELEPORT   (SpellBook.Type.STANDARD, 51, MagicBook.Standard.ARDOUGNE_TELEPORT, new Pair<>(2, RuneElement.LAW), new Pair<>(2, RuneElement.WATER)),
    KOUREND_TELEPORT	(SpellBook.Type.STANDARD, 69, MagicBook.Standard.KOUREND_CASTLE_TELEPORT, new Pair<>(2, RuneElement.LAW), new Pair<>(2, RuneElement.SOUL),new Pair<>(4, RuneElement.WATER), new Pair<>(5, RuneElement.FIRE))
    ;

    private SpellBook.Type spellBookType;
    private int requiredLevel;
    private net.runelite.rsb.internal.globval.enums.Spell rsbSpell;
    private Pair<Integer, RuneElement>[] recipe;

    Spell(SpellBook.Type spellBookType, int level, net.runelite.rsb.internal.globval.enums.Spell rsbSpell, Pair<Integer, RuneElement>... recipe){
        this.spellBookType = spellBookType;
        this.requiredLevel = level;
        this.rsbSpell = rsbSpell;
        this.recipe = recipe;
    }

    public Pair<Integer, RuneElement>[] getRecipe(){
        return recipe;
    }

    public boolean cast() {
        return canUse() && Web.methods.magic.castSpell(rsbSpell);
    }

    @Override
    public boolean canUse(){
        if (SpellBook.getCurrentSpellBook() != spellBookType){
            return false;
        }
        if (requiredLevel > Web.methods.skills.getCurrentLevel(Skill.PRAYER.ordinal())){
            return false;
        }
        if (this == ARDOUGNE_TELEPORT && Web.methods.clientLocalStorage.getVarpValueAt(165) < 30){
            return false;
        }

        for (Pair<Integer, RuneElement> pair : recipe){
            int amountRequiredForSpell = pair.getKey();
            RuneElement runeElement = pair.getValue();
            if (runeElement.getCount() < amountRequiredForSpell){
                return false;
            }
        }
        return true;
    }

}
