package TheShadowMod.cards.TheShadow;

import TheShadowMod.TheShadowMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DarkStrike extends AbstractTSCard {
    public static final String ID = TheShadowMod.makeID(DarkStrike.class.getSimpleName());
    public static final String IMG = TheShadowMod.assetPath("img/cards/TheShadow/DarkStrike.png");
    private static final int COST = 0;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public DarkStrike() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = 7;
        this.magicNumber = this.baseMagicNumber = 2;
        this.tags.add(CardTags.STRIKE);

    }


    public void useThisCard(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard card : p.hand.group) {
                    if (card.type == CardType.ATTACK) {
                        if (card instanceof AbstractTSCard) {
                            AbstractTSCard c = (AbstractTSCard) card;

                            AbstractCard b = null;
                            int index = -1;


                            if (!c.isFlip) {
                                if (c.backCard != null) {
                                    b = c.backCard;
                                    index = c.backCardIndex;
                                }


                                c = new DarkStrike();
                                c.backCard = b;
                                c.backCardIndex = index;
                            } else {
                                if (c.backCard != null) {
                                    c.backCard = new DarkStrike();
                                    c.backCardIndex = 25;
                                }
                            }


                        } else {
                            card = new DarkStrike();
                            ((DarkStrike) card).initializeBackCard();
                        }

                    }
                }
                isDone = true;
            }
        });
    }

    public void thisUpgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(3);
        }
    }
}
