using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;

namespace Blackjack
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        const int CARD_IMAGE_HEIGHT = 280;
        const string IMAGE_PATH = "pack://application:,,,/Images/";
        const string IMAGE_EXT = ".png";

        bool game_over = true;
        string game_msg = "";
        Gameplay game;

        private readonly DispatcherTimer timer = new DispatcherTimer();
        private DateTime endTime;

        public MainWindow()
        {
            InitializeComponent();

             game = new Gameplay();

            DrawHiddenDealerCard();
            DrawPlayerCard();
            DrawDeck();

            gamemsgLabel.Content = "";
            game_over = false;


        }

        // dealer only show 1 card case
        private void DrawHiddenDealerCard()
        {
            dealercardListBox.Items.Clear();
           
            bool first = true;
            foreach (var c in game.dealer_hand)
            {
                string image_path = IMAGE_PATH + "0" + IMAGE_EXT;
                if (first)
                {
                    image_path = IMAGE_PATH + (13 * (int)c.MySuit + (int)c.MyValue) + IMAGE_EXT;
                    first = false;
                }
              
                BitmapImage bitmap = new BitmapImage();
                bitmap.BeginInit();
                bitmap.UriSource = new Uri(image_path);
                bitmap.EndInit();

                Image image = new Image();
                image.Height = CARD_IMAGE_HEIGHT;
                image.HorizontalAlignment = HorizontalAlignment.Center;
                image.VerticalAlignment = VerticalAlignment.Center;
                image.Source = bitmap;

                dealercardListBox.Items.Add(image);
            }
            int value = (int)game.dealer_hand[0].MyValue;

            if(value > 10)
                value = 10;

            dealerLabel.Content = "Dealer's Card          Current Card Value : Greater than " + value;
        }
        // dealer show all card case
        private void DrawDealerCard()
        {
            dealercardListBox.Items.Clear();
            foreach (var c in game.sorted_dealer_hand)
            {
                string image_path = IMAGE_PATH + (13 * (int)c.MySuit + (int)c.MyValue) + IMAGE_EXT;
                BitmapImage bitmap = new BitmapImage();
                bitmap.BeginInit();
                bitmap.UriSource = new Uri(image_path);
                bitmap.EndInit();

                Image image = new Image();
                image.Height = CARD_IMAGE_HEIGHT;
                image.HorizontalAlignment = HorizontalAlignment.Center;
                image.VerticalAlignment = VerticalAlignment.Center;
                image.Source = bitmap;

                dealercardListBox.Items.Add(image);
            }
            dealerLabel.Content = "Dealer's Card          Current Card Value : " + (int)game.GetDealerCardValue();
        }
        // show all player card
        private void DrawPlayerCard()
        {
            playercardListBox.Items.Clear();
            foreach (var c in game.player_hand)
            {
                string image_path = IMAGE_PATH + (13 * (int)c.MySuit + (int)c.MyValue) + IMAGE_EXT;
                BitmapImage bitmap = new BitmapImage();
                bitmap.BeginInit();
                bitmap.UriSource = new Uri(image_path);
                bitmap.EndInit();

                Image image = new Image();
                image.Height = CARD_IMAGE_HEIGHT;
                image.HorizontalAlignment = HorizontalAlignment.Center;
                image.VerticalAlignment = VerticalAlignment.Center;
                image.Source = bitmap;

                playercardListBox.Items.Add(image);
            }
            playerLabel.Content = "Your's hand            Current Card Value : " + game.GetPlayerCardValue() + "            Points : " + game.GetPoints();
        }

        private void DrawDeck()
        {
            if(game.deck.Count <=0)
                deckImage.Visibility = Visibility.Hidden;
            else
                deckImage.Visibility = Visibility.Visible;

            if (game.discard.Count <= 0)
                discardImage.Visibility = Visibility.Hidden;
            else
                discardImage.Visibility = Visibility.Visible;

            deckcardnumberLabel.Content = "Draw Deck :" + game.deck.Count;
            discardcardnumberLabel.Content ="Discard Deck :" + game.discard.Count;

        }
   
        private void Hit_Button_Click(object sender, RoutedEventArgs e)
        {
            if (game_over)
                return;

            game.PlayerDraw();
            DrawPlayerCard();
            DrawDeck();

            if (game.deck.Count <= 0 && game.discard.Count <= 0)
            {
                GameOver();
            }
        }

        private void Stay_Button_Click(object sender, RoutedEventArgs e)
        {
            if (game_over)
                return;

            game.DealerDraw();
            DrawDealerCard();
            DrawDeck();
            GameOver();
        }

        private void GameOver()
        {
            game_over = true;
            int result = game.EvaluateWinner();
            // points
            playerLabel.Content = "Your's hand            Current Card Value : " + game.GetPlayerCardValue() + "            Points : " + game.GetPoints();

            if (result == 1)
            {
                game_msg = "You Won!";
            }
            else if(result == 2)
            {
                game_msg = "You Lose!";
            }
            else
            {
                game_msg = "Push!";
            }
          
            timer.Interval = TimeSpan.FromSeconds(0);
            timer.Tick += new EventHandler(OnTimerTick);
            endTime = DateTime.Now.AddSeconds(5);
            timer.Start();
        }

        private void OnTimerTick(object sender, EventArgs e)
        {
            var now = DateTime.Now;

            if (endTime < now)
            {
                timer.Stop();
                game.SetupRound();
                DrawHiddenDealerCard();
                DrawPlayerCard();
                DrawDeck();

                gamemsgLabel.Content = "";
                game_over = false;
            }
            else
            {
                gamemsgLabel.Content = game_msg + " Next Round Begin in " + (endTime - now).ToString(@"ss") + " seconds";
            }
           
          
        }
    }
}
