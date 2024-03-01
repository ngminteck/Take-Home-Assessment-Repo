using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Blackjack
{
    internal class Card
    {
     
        public enum SUIT
        {
            DIAMONDS =0,
            CLUBS,
            HEARTS,
            SPADES
        }

        public enum VALUE
        {
            ACES = 1, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
        }

        public SUIT MySuit { get; set; }
        public VALUE MyValue { get; set; }

    }


}
