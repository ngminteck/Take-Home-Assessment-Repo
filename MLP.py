import requests
import os
import sqlite3 as sql
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split
from sklearn.model_selection import GridSearchCV
from sklearn.tree import DecisionTreeClassifier
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestClassifier
from xgboost import XGBClassifier
from sklearn.metrics import confusion_matrix

class MLP:
    def __init__(self):
        self.df = None
        self.x = None
        self.y = None
        self.x_train = None
        self.x_test = None
        self.y_train = None
        self.y_test = None
        self.model_param={
            'DecisionTreeClassifier': {
                'model': DecisionTreeClassifier(),
                'param':{
                    'criterion': ['gini'],
                    'max_depth': range(2, 10),
                    'min_samples_split': range(2,100),
                }
            },
            'DecisionTreeRegressor': {
                'model': DecisionTreeRegressor(),
                'param': {
                    'criterion': ['squared_error','friedman_mse'],
                    'max_depth': range(2, 20),
                    'min_samples_split': range(2, 50),
                }
            },
            'RandomForestClassifier': {
                'model': RandomForestClassifier(),
                'param':{
                    'criterion':['gini'],
                    'max_depth': range(2, 4),
                    'n_estimators': [10, 50, 100, 130],
                }
            },
            'XGBClassifier': {
                'model': XGBClassifier(objective='binary:logistic'),
                'param':{
                    'learning_rate': [0.5, 0.1, 0.01, 0.001],
                    'max_depth': range(2, 20),
                    'n_estimators': range(10, 100,10),
                }
            }
        }

    def AddDataFrameFromOnlineDBFile(self, url, query):


        response = requests.get(url, stream = True)

        if response.status_code != 200:
            print("Unable to get response from",url,response.status_code)
            return False

        dest_folder = '../data'

        if not os.path.exists(dest_folder):
            os.makedirs(dest_folder)

        filename = url.split('/')[-1]
        file_path = os.path.join(dest_folder, filename)
        with open(file_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
            f.close()


        try:
            connection = sql.connect(file_path)
        except sql.Error as error:
            raise ValueError(f"unable read the database.\n{error}") from None
            return False

        try:
            self.df = pd.read_sql_query(query, connection)
            connection.close()
        except sql.Error as error:
            connection.close()
            raise ValueError(f"unable success query on the database.\n{error}") from None
            return False


        print(filename, "successfully added to the dataframe list.")
        return True



    def PreProcessData(self):
        print(self.df.head())
        print(self.df.info())
        self.ShowFeaturesUnique()

        try:

            print("Droping column ID, COPD History, Taken Bronchodilators and Dominant Hand.")
            self.df = self.df.drop(columns=['ID', 'COPD History', 'Taken Bronchodilators', 'Dominant Hand'])
            #df = df.drop(columns=['ID', 'Dominant Hand'])
            self.df = self.df.dropna()

            #df['COPD History'] = df['COPD History'].str.lower()
            #df['Taken Bronchodilators'] = df['Taken Bronchodilators'].str.lower()
            #df['COPD History'] = df['COPD History'].map({'yes': 1, 'no': 0}).astype(int)
            #df['Taken Bronchodilators'] = df['Taken Bronchodilators'].map({'yes': 1, 'no': 0}).astype(int)

            print("processing Age column.")
            self.df['Age'] = self.df['Age'].abs().astype(int)
            print("Done processing Age column.")
            print("processing Gender column.")
            self.df['Gender'] = self.df['Gender'].str.lower()
            self.df = self.df.replace('nan', np.nan)
            self.df = self.df.dropna()
            self.df['Gender'] = self.df['Gender'].map({'male': 1, 'female': 0}).astype(int)
            print("Done processing Gender column.")
            print("processing Genetic Markers column.")
            self.df['Genetic Markers'] = self.df['Genetic Markers'].str.lower()
            self.df['Genetic Markers'] = self.df['Genetic Markers'].map({'present': 1, 'not present': 0}).astype(int)
            print("Done processing Genetic Markers column.")
            print("processing Air Pollution Exposure column.")
            self.df['Air Pollution Exposure'] = self.df['Air Pollution Exposure'].str.lower()
            self.df['Air Pollution Exposure'] = self.df['Air Pollution Exposure'].map({'low': 'low exposure', 'medium': 'medium exposure', 'high': 'high exposure'})
            exposure = pd.get_dummies(self.df['Air Pollution Exposure']).astype(int)
            self.df = self.df.drop(columns=['Air Pollution Exposure'])
            self.df = pd.concat([self.df,exposure], axis= 1)
            print("Done processing Air Pollution Exposure column.")
            print("processing Last Weight and Current Weight column.")
            self.df['Last Weight'] = self.df['Last Weight'].abs()
            self.df['Current Weight'] = self.df['Current Weight'].abs()
            print("Done processingLast Weight and Current Weight column.")
            print("Creating Weights Gain Or Loss column.")
            self.df['Weights Gain Or Loss'] = self.df['Current Weight'] - self.df['Last Weight']
            print("Drop Last Weight column.")
            self.df = self.df.drop(columns=['Last Weight'])
            print("processing Start Smoking and Stop Smoking  column.")
            self.df['Start Smoking'] = self.df['Start Smoking'].str.lower()
            self.df['Start Smoking'] = self.df['Start Smoking'].replace('not applicable', '0').astype(int)
            self.df['Stop Smoking'] = self.df['Stop Smoking'].str.lower()
            replacement_dict = {'not applicable': '0', 'still smoking': '2024'}
            self.df['Stop Smoking'] = self.df['Stop Smoking'].replace(replacement_dict).astype(int)
            print("Done processing Start Smoking and Stop Smoking column.")
            print("Creating Smoke Duration column.")
            self.df['Smoke Duration'] = self.df['Stop Smoking'] - self.df['Start Smoking']
            print("Verify Smoke Duration data column.")
            self.df.drop(self.df[self.df['Smoke Duration'] < 0].index, inplace=True)
            self.df['Age Minus Smoke Duration'] = self.df['Age'] - self.df['Smoke Duration']
            self.df.drop(self.df[self.df['Age Minus Smoke Duration'] < 7].index, inplace=True)
            self.df = self.df.drop(columns=['Start Smoking', 'Stop Smoking', 'Age Minus Smoke Duration'])
            print("Done verify Smoke Duration data column.")
            print("processing Frequency of Tiredness column.")
            self.df['Frequency of Tiredness'] = self.df['Frequency of Tiredness'].str.lower()
            self.df['Frequency of Tiredness'] = self.df['Frequency of Tiredness'].map({'none / low': 'freq tired low', 'medium': 'freq tired medium', 'high': 'freq tired high'})
            tiredness = pd.get_dummies(self.df['Frequency of Tiredness']).astype(int)
            self.df = self.df.drop(columns=['Frequency of Tiredness'])
            self.df = pd.concat([self.df, tiredness], axis=1)
            print("Done processing Frequency of Tiredness column.")
            print("processing Lung Cancer Occurrence column.")
            self.df['Lung Cancer Occurrence'] = self.df['Lung Cancer Occurrence'].astype(int)
            print("Done processing Lung Cancer Occurrence column.")
            self.df = self.df.dropna()

            print(self.df.head())
            print(self.df.info())
            self.ShowFeaturesUnique()

            print("Setting x and y.")
            self.x = self.df.drop(labels='Lung Cancer Occurrence', axis=1)
            self.y = self.df['Lung Cancer Occurrence']
            print("Preprocessing done successfully.")
            return True

        except :
            print("PreProcessData Failed.")

            return False

    def ShowFeaturesUnique(self):
        i = 0
        for col in self.df:
            print(self.df.columns[i],self.df[col].unique())
            i += 1


    def SplitData(self):

        self.x_train, self.x_test, self.y_train, self.y_test = train_test_split(self.x, self.y, test_size=0.2, random_state=0)
        print("x train size",len(self.x_train))
        print("x test size", len(self.x_test))


    def ModelSelection(self):

        scores = []
        for model_name, mp in self.model_param.items():
            print(model_name, mp)
            model_selection = GridSearchCV(estimator=mp['model'], param_grid=mp['param'], n_jobs=-1 ,cv=5, return_train_score=False)
            model_selection.fit(self.x, self.y)
            scores.append({
                'model': model_name,
                'best_score': model_selection.best_score_,
                'best_params': model_selection.best_params_
            })
        print(scores)

        #[{'model': 'DecisionTreeClassifier', 'best_score': 0.6470523440537482,
         # 'best_params': {'criterion': 'gini', 'max_depth': 3, 'min_samples_split': 2}},
         #{'model': 'DecisionTreeRegressor', 'best_score': 0.07521307039020957,
          #'best_params': {'criterion': 'friedman_mse', 'max_depth': 8, 'min_samples_split': 18}},
         #{'model': 'RandomForestClassifier', 'best_score': 0.6429723458367694,
          #'best_params': {'criterion': 'gini', 'max_depth': 2, 'n_estimators': 50}},
         #{'model': 'XGBClassifier', 'best_score': 0.6863843054513397,
          #'best_params': {'learning_rate': 0.5, 'max_depth': 11, 'n_estimators': 50}}]

    def XGBClassifierTrain(self):

        dt = XGBClassifier(objective='binary:logistic',learning_rate=0.5,max_depth=11,n_estimators=50)
        dt.fit(self.x_train, self.y_train)

        print("Score on training set: {:.3f}".format(dt.score(self.x_train, self.y_train)))
        print("Score on test set: {:.3f}".format(dt.score(self.x_test, self.y_test)))

        cm = confusion_matrix(self.y_test, dt.predict(self.x_test))
        print(cm)

        sns.heatmap(cm, annot=True)
        plt.xlabel('Predicted')
        plt.ylabel('True Value')
        plt.show()



test = MLP()
status1 = test.AddDataFrameFromOnlineDBFile(".db",'''SELECT * FROM lung_cancer''')
if status1 == True:

    status2 = test.PreProcessData()

    if status2 == True:
        #test.ModelSelection()
        test.SplitData()
        test.XGBClassifierTrain()

