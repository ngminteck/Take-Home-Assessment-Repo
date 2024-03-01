using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blackjack
{

    internal class Gameplay : Card
    {
        public List<Card> deck = new List<Card>();
        public List<Card> discard = new List<Card>();

        public List<Card> player_hand = new List<Card>();
        public List<Card> dealer_hand = new List<Card>();
        public List<Card> sorted_dealer_hand = new List<Card>();

        const int BOOM_CONDITION = 22;
        const int DEALER_DRAW_CONDITION = 17;

        int dealer_card_no = 0;
        int player_card_no = 0;
        int points = 0;

   
        public Gameplay()
        {
            foreach (SUIT s in Enum.GetValues(typeof(SUIT)))
            {
                foreach (VALUE v in Enum.GetValues(typeof(VALUE)))
                {
                    deck.Add(new Card { MySuit = s, MyValue = v });
                }
            }

            Shuffle();
            SetupRound();
        }

        private void Shuffle()
        {
            Random rng = new Random();
            int n = deck.Count;
            while (n > 1)
            {
                n--;
                int k = rng.Next(n + 1);
                var value = deck[k];
                deck[k] = deck[n];
                deck[n] = value;
            }
        }
       
        public void SetupRound()
        {
            // move pervious game card to discard pile
            dealer_hand.ForEach(c => discard.Add(c));
            player_hand.ForEach(c => discard.Add(c));
            
            dealer_hand.Clear();
            player_hand.Clear();
            // check if draw deck below 16 cards or not
            ReShuffle();

            player_hand.Add(deck[0]);
            deck.RemoveAt(0);
            player_hand.Add(deck[0]);
            deck.RemoveAt(0);
            dealer_hand.Add(deck[0]);
            deck.RemoveAt(0);
            dealer_hand.Add(deck[0]);
            deck.RemoveAt(0);

            UpdatePlayerCard();
            UpdateDealerCard();
          
        }

        private void ReShuffle()
        {
            if (discard.Count >=0  && deck.Count <= 15)
            {
                discard.ForEach(c => deck.Add(c));
                discard.Clear();
                Shuffle();
            }
               
        }

        // for player & dealer actually can be refactor from the gameplay class, the player & dealer have quite similar function
        // draw the card and update the card, for this test i just leave it like this 
        private void UpdatePlayerCard()
        {
            player_hand = player_hand.OrderByDescending(x => (int)(x.MyValue)).ToList();
            player_card_no = 0;
            
            foreach (var c in player_hand)
            {
                int value = (int)c.MyValue;
                // J Q K is 10
                if(value > 10)
                {
                    value = 10;
                }
                //Aces case
                if(value == 1)
                {
                    int tmp_number = player_card_no + 10;
                    if(tmp_number < BOOM_CONDITION)
                    {
                        value = 10;
                    }
                   
                }
            
                player_card_no += value;
            }

        }

        private void UpdateDealerCard()
        {
            // sort only for internal calculate dealer current card value.
            sorted_dealer_hand.Clear();
            foreach (var c in dealer_hand)
            {
                sorted_dealer_hand.Add(c);
            }

            sorted_dealer_hand = sorted_dealer_hand.OrderByDescending(x => (int)(x.MyValue)).ToList();
            dealer_card_no = 0;

            foreach (var c in sorted_dealer_hand)
            {
                int value = (int)c.MyValue;
                // J Q K is 10
                if (value > 10)
                {
                    value = 10;
                }
                //Aces case
                if (value == 1)
                {
                    int tmp_number = dealer_card_no + 10;
                    if (tmp_number < BOOM_CONDITION)
                    {
                        value = 10;
                    }

                }

                dealer_card_no += value;
            }

        }

        public void PlayerDraw()
        {
            ReShuffle();
            player_hand.Add(deck[0]);
            deck.RemoveAt(0);
            UpdatePlayerCard();
        }


        public void DealerDraw()
        {
            while (dealer_card_no < DEALER_DRAW_CONDITION)
            {
                ReShuffle();
                dealer_hand.Add(deck[0]);
                deck.RemoveAt(0);
                UpdateDealerCard();
            }

        }

        // int 0 = draw, 1 = win, 2 = lose
        public int EvaluateWinner()
        {
            if (player_card_no < BOOM_CONDITION && dealer_card_no < BOOM_CONDITION)
            {
                if(player_card_no > dealer_card_no)
                {
                    if (player_hand.Count < 3)
                    {
                        points += 15;
                        return 1;
                    }
                    else
                    {
                        points += 10;
                        return 1;
                    }
                }
                else if(player_card_no < dealer_card_no)
                {
                    points -= 10;
                    return 2;
                }
                else
                {
                    return 0;
                }
            }
            else if(player_card_no > BOOM_CONDITION && dealer_card_no < BOOM_CONDITION)
            {
                points -= 10;
                return 2;
            }
            else if(player_card_no < BOOM_CONDITION && dealer_card_no > BOOM_CONDITION)
            {
                if(player_hand.Count < 3)
                {
                    points += 15;
                    return 1;
                }
                else
                {
                    points += 10;
                    return 1;
                }
            }
            else
            {
                return 0;
            }
          
        }

     
        public int GetPlayerCardValue() { return player_card_no; }

        public int GetDealerCardValue() { return dealer_card_no; }

        public int GetPoints() { return points; }
    }

       
}
