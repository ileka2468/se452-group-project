# Roommate Matching Plan Rev.1

## Problem definition
The goal of the roommate matching application is to help DePaul students find other potential roommates based on compatability and preferences. The challenge is to create a matching algorithm that can handle a dynamic and growing user base while ensuring accuracy and scalability. The model should accomodate new users seamlessly and adapt as more data becomes available, improving over time. 

## Approach
The proposed method to achieve this goal is to utilize a modified K-Nearest-Neighbors (KNN) algorithm, adapted for roommate matching based on several living preferences. 

In the original KNN algorithm, the distance between data points is calculated using Euclidean distance, or any other distance formula that treats each feature (in this context, living preference) equally. Such that,

$$d(u_1, u_2) = \sqrt{(x_1 - y_1)^2 + (x_2 - y_2)^2 + \dots}$$

where $u_1, \dots, u_n$ represents users, $x$ refers to preferences of the first user and $y$ representing the preferences of the second user. The issue with applying the original KNN is that we need to utilize known labels (whether a user is a good or bad match based on past data) for training, and for *each* user. Additionally, it would need to be re-calculated each time a user joins the application for the model to learn. This is inefficient and would not help us achieve our goal. 

For a better fitting, we will be modifying the KNN model such that there are no predefined labels such as 'good' or 'bad' matches. Instead of intensively calculating such matches, we will find the **most similar** users based on their preferences; the model will focus on similarity-based matching rather than utilizing prediction or classification. It is also a more **real-time** approach since we purely compare user preferences and output a list of nearest neighbors for a user without needing to reference any labels or historical data. We modify KNN such that,

$$d(u_1, u_2) = \sqrt{w_1 \cdot (x_1 - y_1)^2 + w_2 \cdot (x_2-y_2)^2 + \dots}$$ 

where we scale each difference in preference with a weight $w_n$. Weights allow us to accommodate for user prioritized preferences, for example, smoking habits may be more important than sleeping habit alignment. 

## Handling
The `RoommateMatcherService` will utilize a priority queue called `minHeap` to keep track of nearest neighbors. It will then calculate the distance from the user to every other user and add the `user` and `distance` to the priority queue. It will then initialize a list called `KNN` that stores the **finalized** nearest neighbors from `minHeap` by retrieving and removing the user with the smallest distance (most similar) until we have $K$ amount of users, or until the minHeap is empty.

After determining the nearest neighbors, the `RoommateMatcherService` will then invoke a method from the `MatchStorageService` to save the nearest neighbor as a `RoommateMatch` object and store the match inside the `RoommateMatchesRepository`.

## Filtering
In order to align with user expectations in potential matching, we will implement various filtering methods. Outside its function for the user, it allows for higher match quality.
For example, if someone strongly prefers a non-smoker roommate, filtering ensures that only non-smoker profiles are considered in the finalized candidates.
By applying filtering, we're using a practical approach to simplify and speed up the matching process before running a more complex scoring system.
It acts as a heuristic step that lets the matching system manage complexity and improve practical outcomes without needing to do large and exhaustive calculations.
This helps focus resources (like memory usage and calculations) on profiles with higher potential, making the matching system more efficient and scalable - especially in the scenario
of college roommate matching where we can expect a large amount of profiles.

We filter by gender, smoking, drinking and cleanliness compatibility in this order. This is to derive the smallest potential match pool without entirely restricting
profile addition to `minHeap`. In the event that the value of $K$ is too large, indicating that we want more candidate profiles than what is available in the list of 
most compatible profiles, the system will adjust to make $K$ the size of the compatible profiles pool. This reinforces the availability of the system and supports
the integrity of the returned data. 